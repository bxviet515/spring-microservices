package com.xuanviet.borrowingservice.command.aggregate;

import com.xuanviet.borrowingservice.command.command.CreateBorrowingCommand;
import com.xuanviet.borrowingservice.command.command.DeleteBorrowingCommand;
import com.xuanviet.borrowingservice.command.event.BorrowingCreatedEvent;
import com.xuanviet.borrowingservice.command.event.BorrowingDeletedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@Aggregate
@NoArgsConstructor
public class BorrowingAggregate {
    @AggregateIdentifier
    private String id;
    private String bookId;
    private String employeeId;
    private Date borrowDate;
    private Date returnDate;

    @CommandHandler
    public BorrowingAggregate(CreateBorrowingCommand command){
        BorrowingCreatedEvent event = new BorrowingCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(BorrowingCreatedEvent event){
        this.id = event.getId();
        this.bookId = event.getBookId();
        this.employeeId = event.getEmployeeId();
        this.borrowDate = event.getBorrowDate();

    }

    @CommandHandler
    public void handle(DeleteBorrowingCommand command){
        BorrowingDeletedEvent event = new BorrowingDeletedEvent(command.getId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(BorrowingDeletedEvent event){
        this.id = event.getId();
    }
}
