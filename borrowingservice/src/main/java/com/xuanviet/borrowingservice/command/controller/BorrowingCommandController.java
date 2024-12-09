package com.xuanviet.borrowingservice.command.controller;

import com.xuanviet.borrowingservice.command.command.CreateBorrowingCommand;
import com.xuanviet.borrowingservice.command.data.BorrowingRepository;
import com.xuanviet.borrowingservice.command.model.BorrowingCreateModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowingCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createBorrowing(@RequestParam BorrowingCreateModel model){
        CreateBorrowingCommand command = new CreateBorrowingCommand(UUID.randomUUID().toString(), model.getBookId(), model.getEmployeeId(), new Date());
        return commandGateway.sendAndWait(command);
    }

}
