package com.xuanviet.bookservice.command.aggregate;

import com.xuanviet.bookservice.command.command.CreateBookCommand;
import com.xuanviet.bookservice.command.command.DeleteBookCommand;
import com.xuanviet.bookservice.command.command.UpdateBookCommand;
import com.xuanviet.bookservice.command.event.BookCreatedEvent;
import com.xuanviet.bookservice.command.event.BookDeletedEvent;
import com.xuanviet.bookservice.command.event.BookUpdatedEvent;
import com.xuanviet.commonservice.command.RollBackStatusBookCommand;
import com.xuanviet.commonservice.command.UpdateStatusBookCommand;
import com.xuanviet.commonservice.event.BookRollBackStatusEvent;
import com.xuanviet.commonservice.event.BookUpdateStatusEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class BookAggregate {
    @AggregateIdentifier
    private String id;

    private String name;
    private String author;

    private Boolean isReady;


    @CommandHandler
    public BookAggregate(CreateBookCommand command){
        BookCreatedEvent bookCreatedEvent = new BookCreatedEvent();
        BeanUtils.copyProperties(command, bookCreatedEvent);

        AggregateLifecycle.apply(bookCreatedEvent);
    }

    @CommandHandler
    public void handle(UpdateBookCommand command){
        BookUpdatedEvent bookUpdatedEvent = new BookUpdatedEvent();
        BeanUtils.copyProperties(command, bookUpdatedEvent);
        AggregateLifecycle.apply(bookUpdatedEvent);
    }
    @CommandHandler
    public void handle(DeleteBookCommand command){
        BookDeletedEvent bookDeletedEvent = new BookDeletedEvent();
        BeanUtils.copyProperties(command, bookDeletedEvent);
        AggregateLifecycle.apply(bookDeletedEvent);
    }
    @EventSourcingHandler
    public void on(BookCreatedEvent event){
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookUpdatedEvent event){
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookDeletedEvent event){
        this.id = event.getId();
    }

    @CommandHandler
    public void handler(UpdateStatusBookCommand command){
        BookUpdateStatusEvent event = new BookUpdateStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);

    }


    @EventSourcingHandler
    public void on(BookUpdateStatusEvent event){
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }

    @CommandHandler
    public void handler(RollBackStatusBookCommand command){
        BookRollBackStatusEvent event = new BookRollBackStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(BookRollBackStatusEvent event){
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }
}
