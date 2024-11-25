package com.xuanviet.bookservice.query.projection;

import com.xuanviet.bookservice.command.data.Book;
import com.xuanviet.bookservice.command.data.BookRepository;
import com.xuanviet.bookservice.command.model.BookRequestModel;
import com.xuanviet.bookservice.query.model.BookResponseModel;
import com.xuanviet.bookservice.query.queries.GetAllBookQuery;
import com.xuanviet.bookservice.query.queries.GetBookDetailQuery;
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
    public BookResponseModel handle(GetBookDetailQuery query){
        BookResponseModel bookResponseModel = new BookResponseModel();
        bookRepository.findById(query.getId()).ifPresent(book -> {

            BeanUtils.copyProperties(book, bookResponseModel);

        });
        return bookResponseModel;
    }
}
