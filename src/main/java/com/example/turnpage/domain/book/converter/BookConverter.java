package com.example.turnpage.domain.book.converter;

import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookPageElement;
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

    public BookPageElement toBookPageElement(Book book) {
        return BookPageElement.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .cover(book.getCover())
                .publisher(book.getPublisher())
                .publicationDate(book.getPublicationDate())
                .rank(book.getRanking())
                .star(book.getStar())
                .build();
    }

    public BookPageInfos toBookPageInfos(Page<Book> books) {

        List<BookPageElement> bookPageElements = books.stream()
                .map(this::toBookPageElement).toList();

        return BookPageInfos.builder()
                .bookPageElements(bookPageElements)
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
