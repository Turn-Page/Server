package com.example.turnpage.domain.book.service;


import com.example.turnpage.domain.book.client.BestSellerClient;
import com.example.turnpage.domain.book.converter.BookConverter;
import com.example.turnpage.domain.book.dto.BookResponse.BookId;
import com.example.turnpage.domain.book.dto.BookResponse.BookDetailInfo;
import com.example.turnpage.domain.book.dto.BookResponse.BookPageInfos;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.repository.BookRepository;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.BookErrorCode;
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
        List<Long> oldBestSellerItemId =  bookRepository.findAllItemIdByRankingNotNull();
        // rank 초기화
        bookRepository.updateRankToNull();

        bestSellerBooks.forEach(book -> {
            if(oldBestSellerItemId.contains(book.getItemId()))
                updateRanking(book.getItemId(),book.getRank());
            else saveBook(book);
        });

    }

    private void updateRanking(Long itemId, int rank) {
        Book book = findBookByItemId(itemId).orElseThrow(() -> new BusinessException(BookErrorCode.BOOK_NOT_FOUND));
        book.updateRanking(rank);
    }

    @Override
   public BookPageInfos fetchBestSeller(Pageable pageable) {
        return bookConverter.toBookPageInfos(
                bookRepository.findAllByRankingNotNullOrderByRanking(pageable));
    }

    @Override
    public BookDetailInfo getBookDetailInfo(Long bookId) {
        return bookConverter.toBookDetailInfo(findBook(bookId));
    }

    @Override
    public BookPageInfos searchBook(String keyword, Pageable pageable) {
        keyword = keyword.replace(" ","");
        return bookConverter.toBookPageInfos(
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
