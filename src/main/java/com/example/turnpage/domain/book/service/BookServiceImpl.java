package com.example.turnpage.domain.book.service;


import com.example.turnpage.domain.book.client.BestSellerClient;
import com.example.turnpage.domain.book.converter.BookConverter;
import com.example.turnpage.domain.book.dto.BookResponse.BookDetailInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookId;
import com.example.turnpage.domain.book.dto.BookResponse.PagedBookInfo;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.repository.BookRepository;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.code.BookErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return new BookId(saveBookInfo(request).getId());
    }

    @Override
    @Transactional
    public Book saveBookInfo(SaveBookRequest request) {
        String coverUrl = changeCoverImageSize(request.getCover());
        return bookRepository.save(bookConverter.toEntity(request.getItemId(),
                request.getTitle(),request.getAuthor(), coverUrl, request.getIsbn(),
                request.getPublisher(), request.getPublicationDate(),
                request.getDescription(), request.getRank()));
    }

    @Override
    @Transactional
    public void saveBestSeller() {
        //이미 있는 BOOK인지 확인, 있는 BOOK이면 rank update, 없는 BOOK이면 saveBook
        List<SaveBookRequest> bestSellerBooks = bestSellerClient.getBestSellerBooks();
        bookRepository.updateRankToNull();
        bookRepository.flush(); // 변경 내용 데이터베이스에 반영

        // 랭킹 업데이트
        bestSellerBooks.forEach(b -> {
            Optional<Book> book = findBookByItemId(b.getItemId());
            if (book.isPresent()) {
                book.get().updateRanking(b.getRank());
            } else {
                saveBook(b); // 존재하지 않는 책은 새로 저장
            }
        });
    }
    @Override
   public PagedBookInfo fetchBestSeller(Pageable pageable) {
        return bookConverter.toPagedBookInfo(
                bookRepository.findAllByRankingNotNullOrderByRanking(pageable));
    }

    @Override
    public BookDetailInfo getBookDetailInfo(Long bookId) {
        return bookConverter.toBookDetailInfo(findBook(bookId));
    }

    @Override
    public PagedBookInfo searchBook(String keyword, Pageable pageable) {
        keyword = keyword.replace(" ","");
        return bookConverter.toPagedBookInfo(
                bookRepository.findByTitleOrAuthorContaining(keyword, pageable));
    }

    @Override
    public Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(BookErrorCode.BOOK_NOT_FOUND));
    }

    @Override
    public Optional<Book> findBookByItemId(Long itemId) {
        return bookRepository.findByItemId(itemId);
    }

    private String changeCoverImageSize(String cover) {
        return cover.replace("coversum", "cover500");
    }
}
