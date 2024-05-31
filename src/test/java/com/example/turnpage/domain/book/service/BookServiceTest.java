package com.example.turnpage.domain.book.service;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.book.dto.BookResponse;
import com.example.turnpage.domain.book.dto.BookResponse.BookDetailInfo;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@DisplayName("Book Service 의")
public class BookServiceTest extends ServiceTestConfig {
    @Autowired BookService bookService;

    @Test
    @Transactional
    @DisplayName("도서 정보 저장 성공")
    public void saveBook() {
        //given
        SaveBookRequest request = SaveBookRequest.builder()
                .itemId(1L)
                .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                .author("브로디, 노아")
                .cover("https://image.aladin.co.kr/product/33948/74/coversum/k392930236_1.jpg")
                .isbn("12987349382")
                .publisher("포레스트북스")
                .publicationDate("2023-12-18")
                .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                .build();

        //when
        Book book = bookService.findBook(bookService.saveBook(request).getBookId());

        //then
        assertEquals(1L, book.getItemId());
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그", book.getTitle());
        assertEquals("브로디, 노아", book.getAuthor());
        assertEquals("12987349382", book.getIsbn());
        assertEquals("포레스트북스", book.getPublisher());
        assertEquals("https://image.aladin.co.kr/product/33948/74/cover500/k392930236_1.jpg", book.getCover());
        assertEquals("2023-12-18", book.getPublicationDate());
        assertEquals("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.", book.getDescription());
        assertEquals(0, book.getStar());

    }

    @Test
    @Transactional
    @DisplayName("베스트셀러 조회 성공")
    public void fetchBestseller() {
        //given
        for(int i=1;i<=11;i++) {
            SaveBookRequest book = SaveBookRequest.builder()
                    .rank(i)
                    .itemId(1L +i)
                    .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                    .author("브로디, 노아")
                    .isbn(Integer.toString(i))
                    .publisher("포레스트북스")
                    .cover("https://image.aladin.co.kr/product/33948/74/cover500/k392930236_1.jpg")
                    .publicationDate("2023-12-18")
                    .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                    .build();

            bookService.saveBook(book);
        }
        //when
        Pageable pageable = PageRequest.of(0, 20);
        BookResponse.PagedBookInfo bestSellerInfos = bookService.fetchBestSeller(pageable);

        //then
        assertEquals(11, bestSellerInfos.getTotalElements());
        assertEquals(1, bestSellerInfos.getBookInfoList().get(0).getRank());
    }

    @Test
    @Transactional
    @DisplayName("도서 상세 조회 성공")
    public void getBookDetailInfo() {
        //given
        SaveBookRequest request = SaveBookRequest.builder()
                .rank(null)
                .itemId(1L)
                .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                .author("브로디, 노아")
                .isbn("12987349382")
                .publisher("포레스트북스")
                .cover("https://image.aladin.co.kr/product/33948/74/cover500/k392930236_1.jpg")
                .publicationDate("2023-12-18")
                .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                .build();

        Book book = bookService.findBook(bookService.saveBook(request).getBookId());

        //when
        BookDetailInfo bookDetailInfo = bookService.getBookDetailInfo(book.getId());

        //then
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그", bookDetailInfo.getTitle());
        assertEquals("브로디, 노아", bookDetailInfo.getAuthor());
        assertEquals("12987349382", bookDetailInfo.getIsbn());
        assertEquals("포레스트북스", bookDetailInfo.getPublisher());
        assertEquals("https://image.aladin.co.kr/product/33948/74/cover500/k392930236_1.jpg", bookDetailInfo.getCover());
        assertEquals("2023-12-18", bookDetailInfo.getPublicationDate());
        assertEquals("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.", bookDetailInfo.getDescription());
        assertEquals(0, bookDetailInfo.getStar());
        assertNull(bookDetailInfo.getRank());

    }

    @Test
    @Transactional
    @DisplayName("도서 검색 성공")
    public void searchBook() {
        ///given
        SaveBookRequest request = SaveBookRequest.builder()
                .itemId(1L)
                .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                .author("브로디, 노아")
                .cover("https://image.aladin.co.kr/product/33948/74/coversum/k392930236_1.jpg")
                .isbn("12987349382")
                .publisher("포레스트북스")
                .publicationDate("2023-12-18")
                .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                .build();
        bookService.saveBook(request);

        //when
        Pageable pageable = PageRequest.of(0, 20);
        BookResponse.PagedBookInfo bookPageInfos = bookService.searchBook("목표", pageable);

        //then
        assertEquals(1, bookPageInfos.getTotalPages());
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그",
                bookPageInfos.getBookInfoList().get(0).getTitle());

    }

}
