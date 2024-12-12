package com.xuanviet.borrowingservice.command.saga;

import com.xuanviet.borrowingservice.command.command.DeleteBorrowingCommand;
import com.xuanviet.borrowingservice.command.event.BorrowingCreatedEvent;
import com.xuanviet.commonservice.command.UpdateStatusBookCommand;
import com.xuanviet.commonservice.event.BookUpdateStatusEvent;
import com.xuanviet.commonservice.model.BookResponseCommonModel;
import com.xuanviet.commonservice.queries.GetBookDetailQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
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
        SagaLifecycle.end();
    }

    private void rollbackBorrowingRecord(String id){
        DeleteBorrowingCommand command = new DeleteBorrowingCommand(id);
        commandGateway.sendAndWait(command);
        SagaLifecycle.end();
    }
}
