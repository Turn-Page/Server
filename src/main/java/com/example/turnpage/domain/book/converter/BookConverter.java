package com.example.turnpage.domain.book.converter;

import com.example.turnpage.domain.book.dto.BookResponse;
import com.example.turnpage.domain.book.dto.BookResponse.BookDetailInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookInfo;
import com.example.turnpage.domain.book.dto.BookResponse.PagedBookInfo;
import com.example.turnpage.domain.book.dto.BookResponse.SimpleBookInfo;
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

    public BookInfo toBookInfo(Book book) {
        return BookInfo.builder()
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

    public SimpleBookInfo toSimpleBookInfo(Book book) {
        return SimpleBookInfo.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .cover(book.getCover())
                .build();
    }

    public PagedBookInfo toPagedBookInfo(Page<Book> books) {

        List<BookInfo> bookInfoList = books.stream()
                .map(this::toBookInfo).toList();

        return PagedBookInfo.builder()
                .bookInfoList(bookInfoList)
                .page(books.getNumber())
                .totalPages(books.getTotalPages())
                .totalElements(books.getTotalElements())
                .isFirst(books.isFirst())
                .isLast(books.isLast())
                .build();
    }

    public BookDetailInfo toBookDetailInfo(Book book) {
        return BookDetailInfo.builder()
                .bookId(book.getId())
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
