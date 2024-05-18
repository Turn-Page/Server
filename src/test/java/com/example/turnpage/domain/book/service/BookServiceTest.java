package com.example.turnpage.domain.book.service;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.book.dto.BookResponse;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static com.example.turnpage.domain.book.dto.BookResponse.BookPageInfos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@DisplayName("Book Service의")
public class BookServiceTest extends ServiceTestConfig {
    @Autowired BookService bookService;

    @Test
    @Transactional
    public void 도서_정보_저장() {
        //given
        SaveBookRequest request = SaveBookRequest.builder()
                .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                .author("브로디, 노아")
                .cover("https://image.aladin.co.kr/product/33948/74/coversum/k392930236_1.jpg")
                .isbn("12987349382")
                .itemId(1L)
                .publisher("포레스트북스")
                .publicationDate("2023-12-18")
                .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                .build();

        //when
        Book book = bookService.findBook(bookService.saveBook(request).getBookId());

        //then
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그", book.getTitle());
        assertEquals("12987349382", book.getIsbn());
        assertEquals("브로디, 노아", book.getAuthor());
        assertEquals("https://image.aladin.co.kr/product/33948/74/cover500/k392930236_1.jpg", book.getCover());
        assertEquals("2023-12-18", book.getPublicationDate());
        assertEquals("포레스트북스", book.getPublisher());
        assertEquals("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.", book.getDescription());
        assertEquals(1L, book.getItemId());

    }

    @Test
    @Transactional
    public void 베스트셀러_조회() {
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
        BookPageInfos bestSellerInfos = bookService.fetchBestSeller(pageable);

        //then
        assertEquals(11, bestSellerInfos.getTotalBooks());
        assertEquals(1, bestSellerInfos.getBestSellerInfos().get(0).getRank());
    }

    @Test
    @Transactional
    public void 도서_상세_조회() {
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
        BookResponse.BookInfo bookInfo = bookService.getBookInfo(book.getId());

        //then
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그", bookInfo.getTitle());
        assertEquals("12987349382", bookInfo.getIsbn());
        assertEquals("브로디, 노아", bookInfo.getAuthor());
        assertEquals("https://image.aladin.co.kr/product/33948/74/cover500/k392930236_1.jpg", bookInfo.getCover());
        assertEquals("2023-12-18", bookInfo.getPublicationDate());
        assertEquals("포레스트북스", bookInfo.getPublisher());
        assertEquals("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.", bookInfo.getDescription());
        assertEquals(0, bookInfo.getStar());
        assertNull(bookInfo.getRank());

    }

}
