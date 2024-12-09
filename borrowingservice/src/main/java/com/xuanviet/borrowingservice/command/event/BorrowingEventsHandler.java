package com.xuanviet.borrowingservice.command.event;

import com.xuanviet.borrowingservice.command.data.Borrowing;
import com.xuanviet.borrowingservice.command.data.BorrowingRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowingEventsHandler {
    @Autowired
    private BorrowingRepository borrowingRepository;
    @EventHandler
    public void on(BorrowingCreatedEvent event){
        Borrowing model = new Borrowing();
        model.setId(event.getId());
        model.setBookId(event.getBookId());
        model.setEmployeeId(event.getEmployeeId());
        model.setBorrowDate(event.getBorrowDate());
        borrowingRepository.save(model);
    }
}