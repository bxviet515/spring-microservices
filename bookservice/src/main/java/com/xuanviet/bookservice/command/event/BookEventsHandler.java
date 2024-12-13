package com.xuanviet.bookservice.command.event;

import com.xuanviet.bookservice.command.data.Book;
import com.xuanviet.bookservice.command.data.BookRepository;
import com.xuanviet.commonservice.event.BookRollBackStatusEvent;
import com.xuanviet.commonservice.event.BookUpdateStatusEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookEventsHandler {
    @Autowired
    private BookRepository bookRepository;
    @EventHandler
    public void on(BookCreatedEvent event){
        Book book = new Book();
        BeanUtils.copyProperties(event, book);
        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookUpdatedEvent event){
        Optional<Book> oldBook = bookRepository.findById(event.getId());
        if(oldBook.isPresent()){
            Book book = oldBook.get();
            book.setAuthor(event.getAuthor());
            book.setName(event.getName());
            book.setIsReady(event.getIsReady());

            bookRepository.save(book);
        }
    }

    @EventHandler
    public void on(BookUpdateStatusEvent event){
        Optional<Book> oldBook = bookRepository.findById(event.getBookId());
        oldBook.ifPresent(book -> {
            book.setIsReady(event.getIsReady());
            bookRepository.save(book);
        });
    }
    @EventHandler
    public void on(BookDeletedEvent event){
        Optional<Book> oldBook = bookRepository.findById(event.getId());
        oldBook.ifPresent(book -> bookRepository.delete(book));
    }

    @EventHandler
    public void on(BookRollBackStatusEvent event){
        Optional<Book> oldBook = bookRepository.findById(event.getBookId());
        oldBook.ifPresent(book -> {
            book.setIsReady(event.getIsReady());
            bookRepository.save(book);
        });
    }
}
