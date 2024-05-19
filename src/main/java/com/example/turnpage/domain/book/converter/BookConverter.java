package com.example.turnpage.domain.book.converter;

import com.example.turnpage.domain.book.dto.BookResponse.BestSellerInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookPageInfos;
import com.example.turnpage.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookConverter {
    public Book toEntity(Long itemId, String title, String author, String cover, String isbn,
                         String publisher, String publicationDate, String description, Integer rank) {
        return Book.builder()
                .itemId(itemId)
                .title(title)
                .author(author)
                .cover(cover)
                .isbn(isbn)
                .publisher(publisher)
                .publicationDate(publicationDate)
                .description(description)
                .ranking(rank)
                .star(0.0)
                .build();
    }

    public BestSellerInfo toBestSellerInfo(Book book) {
        return BestSellerInfo.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .cover(book.getCover())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publicationDate(book.getPublicationDate())
                .description(book.getDescription())
                .rank(book.getRanking())
                .build();
    }

    public BookPageInfos toBookPageInfos(Page<Book> books) {

        List<BestSellerInfo> bestSellerInfos = books.stream()
                .map(this::toBestSellerInfo).toList();

        return BookPageInfos.builder()
                .bestSellerInfos(bestSellerInfos)
                .page(books.getNumber())
                .totalPages(books.getTotalPages())
                .totalBooks((int) books.getTotalElements())
                .isFirst(books.isFirst())
                .isLast(books.isLast())
                .build();
    }

    public BookInfo toBookInfo(Book book) {
        return BookInfo.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .cover(book.getCover())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publicationDate(book.getPublicationDate())
                .description(book.getDescription())
                .rank(book.getRanking())
                .star(book.getStar())
                .build();

    }
}
