package com.example.turnpage.domain.book.controller;

import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Book 컨트롤러의")
public class BookControllerTest extends ControllerTestConfig {
    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("책 정보 저장 테스트")
    public void saveBookTest() throws Exception {

        //given
        final String url = "/books";
        SaveBookRequest request = SaveBookRequest.builder()
                .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                .author("브로디, 노아")
                .cover("https://image.aladin.co.kr/product/33948/74/coversum/k392930236_1.jpg")
                .isbn("12987349382")
                .publisher("포레스트북스")
                .publicationDate("2023-12-18")
                .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                .build();

        BookId response = new BookId(1L);

        given(bookService.saveBook(any(SaveBookRequest.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SB001"))
                .andExpect(jsonPath("$.data.isbn").value("12987349382"))
        ;

        verify(bookService).saveBook(any(SaveBookRequest.class));
    }

    @Test
    @DisplayName("베스트셀러 조회 테스트")
    public void fetchBestSeller() throws Exception {

        //given
        final String url = "/books/bestSeller";

        BookPageInfos response = new  BookPageInfos.<BestSellerInfo>builder()
                .page(0)
                .totalPages(1)
                .totalBooks(1)
                .bestSellerInfos(new ArrayList<BestSellerInfo>())
                .isFirst(true)
                .isLast(false)
                .build();

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Order.desc("rank")));

        given(bookService.fetchBestSeller(any(Pageable.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
                get(url).param("page", "0"));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SB002"))
                .andExpect(jsonPath("$.data.totalBooks").value(1))
        ;

        verify(bookService).fetchBestSeller(any(Pageable.class));
    }
}
