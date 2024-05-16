package com.example.turnpage.domain.book.service;

import com.example.turnpage.domain.book.dto.BookResponse.BookId;
import com.example.turnpage.domain.book.entity.Book;

import static com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;

public interface BookService {
    Book findBook(Long bookId);
    BookId saveBook(SaveBookRequest request);
    void saveBestSeller();


}
