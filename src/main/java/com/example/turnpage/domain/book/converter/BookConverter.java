package com.example.turnpage.domain.book.converter;

import com.example.turnpage.domain.book.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {
    public Book toEntity(String title, String author, String cover, String isbn,
                         String publisher, String publicationDate, String description, Integer rank) {
        return Book.builder()
                .title(title)
                .author(author)
                .cover(cover)
                .isbn(isbn)
                .publisher(publisher)
                .publicationDate(publicationDate)
                .description(description)
                .bestSellerRank(rank)
                .build();
    }
}
