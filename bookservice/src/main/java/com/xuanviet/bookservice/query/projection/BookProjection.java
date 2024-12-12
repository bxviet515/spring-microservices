package com.xuanviet.bookservice.query.projection;

import com.xuanviet.bookservice.command.data.Book;
import com.xuanviet.bookservice.command.data.BookRepository;
import com.xuanviet.bookservice.query.model.BookResponseModel;
import com.xuanviet.bookservice.query.queries.GetAllBookQuery;
import com.xuanviet.commonservice.model.BookResponseCommonModel;
import com.xuanviet.commonservice.queries.GetBookDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookProjection {
    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBookQuery query){
        List<Book> list = bookRepository.findAll();
        List<BookResponseModel> bookList = new ArrayList<>();
        list.forEach(book ->{
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            bookList.add(model);
        });
        return bookList;
    }

    @QueryHandler
    public BookResponseCommonModel handle(GetBookDetailQuery query) throws Exception {
        BookResponseCommonModel bookResponseModel = new BookResponseCommonModel();
        Book book = bookRepository.findById(query.getId()).orElseThrow(() -> new Exception("Not found Book with id: "+query.getId()));


        BeanUtils.copyProperties(book, bookResponseModel);


        return bookResponseModel;
    }
}
