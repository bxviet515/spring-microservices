package com.xuanviet.bookservice.query.controller;

import com.xuanviet.bookservice.query.model.BookResponseModel;
import com.xuanviet.bookservice.query.queries.GetAllBookQuery;
import com.xuanviet.commonservice.model.BookResponseCommonModel;
import com.xuanviet.commonservice.queries.GetBookDetailQuery;
import com.xuanviet.commonservice.services.KafkaService;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private KafkaService kafkaService;
    @GetMapping
    public List<BookResponseModel> getAllBooks(){
        GetAllBookQuery query = new GetAllBookQuery();
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(BookResponseModel.class)).join();
    }

    @GetMapping("/{bookId}")
    public BookResponseCommonModel getBookDetail(@PathVariable String bookId){
        GetBookDetailQuery query = new GetBookDetailQuery(bookId);
        return queryGateway.query(query, ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();
    }

    @PostMapping("/sendMessage")
    public void sendMessage(String message){
        kafkaService.sendMessage("test", message);
    }
}
