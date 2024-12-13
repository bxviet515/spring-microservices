package com.xuanviet.borrowingservice.command.saga;

import com.xuanviet.borrowingservice.command.command.DeleteBorrowingCommand;
import com.xuanviet.borrowingservice.command.event.BorrowingCreatedEvent;
import com.xuanviet.borrowingservice.command.event.BorrowingDeletedEvent;
import com.xuanviet.commonservice.command.RollBackStatusBookCommand;
import com.xuanviet.commonservice.command.UpdateStatusBookCommand;
import com.xuanviet.commonservice.event.BookRollBackStatusEvent;
import com.xuanviet.commonservice.event.BookUpdateStatusEvent;
import com.xuanviet.commonservice.model.BookResponseCommonModel;
import com.xuanviet.commonservice.model.EmployeeResponseCommonModel;
import com.xuanviet.commonservice.queries.GetBookDetailQuery;
import com.xuanviet.commonservice.queries.GetDetailEmployeeQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
@Slf4j
public class BorrowingSaga {
    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    public void handle(BorrowingCreatedEvent event){
        try{
            GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery(event.getBookId());
            BookResponseCommonModel bookResponseCommonModel = queryGateway.query(getBookDetailQuery,
                    ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();
            if(!bookResponseCommonModel.getIsReady()){
                throw new Exception("Sach da co nguoi muon");
            }else{
                SagaLifecycle.associateWith("bookId", event.getBookId());
                UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false, event.getEmployeeId(), event.getId());
                commandGateway.sendAndWait(command);
            }
        }catch (Exception e){
            rollbackBorrowingRecord(event.getId());
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handler(BookUpdateStatusEvent event){
        log.info("Book update in Saga with id: "+event.getBookId());
        try{
            GetDetailEmployeeQuery query = new GetDetailEmployeeQuery(event.getEmployeeId());
            EmployeeResponseCommonModel employeeModel = queryGateway.query(query, ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            if(employeeModel.getIsDisciplined()){
                throw new Exception("Nhan vien bi ky luat");
            }else{
                log.info("Da muon sach thanh cong");
                SagaLifecycle.end();
            }

        }catch (Exception ex){
            rollbackBookStatus(event.getBookId(), event.getEmployeeId(), event.getBorrowingId());
            log.error(ex.getMessage());
        }

    }


    private void rollbackBorrowingRecord(String id){
        DeleteBorrowingCommand command = new DeleteBorrowingCommand(id);
        commandGateway.sendAndWait(command);

    }

    private void rollbackBookStatus(String bookId, String employeeId, String borrowingId){
        SagaLifecycle.associateWith("bookId", bookId);
        RollBackStatusBookCommand command = new RollBackStatusBookCommand(bookId, true, employeeId, borrowingId);
        commandGateway.sendAndWait(command);
        rollbackBorrowingRecord(bookId);
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handle(BookRollBackStatusEvent event){
        log.info("BookRollBackStatusEvent in Saga for book id: "+event.getBookId());
        rollbackBorrowingRecord(event.getBorrowingId());
    }

    @SagaEventHandler(associationProperty = "id")
    @EndSaga
    private void handle(BorrowingDeletedEvent event){
        log.info("BorrowDeletedEvent in Saga for borrowing id: "+event.getId());
        SagaLifecycle.end();
    }
}
