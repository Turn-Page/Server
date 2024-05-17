package com.example.turnpage.domain.book.service;

import com.example.turnpage.domain.book.dto.BookResponse;
import com.example.turnpage.domain.book.dto.BookResponse.BookId;
import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookPageInfos;
import com.example.turnpage.domain.book.entity.Book;
import org.springframework.data.domain.Pageable;

import static com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;

public interface BookService {
    Book findBook(Long bookId);
    BookId saveBook(SaveBookRequest request);
    void saveBestSeller();
    BookPageInfos fetchBestSeller(Pageable pageable);
    BookInfo getBookInfo(Long bookId);


}
