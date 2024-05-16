package com.example.turnpage.domain.book.service;


import com.example.turnpage.domain.book.converter.BookConverter;
import com.example.turnpage.domain.book.dto.BookResponse.BookId;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.repository.BookRepository;
import com.example.turnpage.domain.book.client.BestSellerClient;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.BookErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    private final BestSellerClient bestSellerClient;

    @Override
    @Transactional
    public BookId saveBook(SaveBookRequest request) {
        String coverUrl = changeCoverImageSize(request.getCover());

        Book book = bookRepository.save(bookConverter.toEntity(
                request.getTitle(),request.getAuthor(), coverUrl, request.getIsbn(),
                request.getPublisher(), request.getPublicationDate(),
                request.getDescription(), request.getRank()));

        return new BookId(book.getId());
    }

    @Override
    @Transactional
    public void saveBestSeller() {
        List<SaveBookRequest> bestSellerBooks = bestSellerClient.getBestSellerBooks();
        //TODO : 이미 있는 BOOK인지 확인, 있는 BOOK이면 rank update, 없는 BOOK이면 saveBook
        bestSellerBooks.forEach(this::saveBook);
    }

    @Override
    public Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(BookErrorCode.BOOK_NOT_FOUND));
    }

    private String changeCoverImageSize(String cover) {
        return cover.replace("coversum", "cover500");
    }
}
