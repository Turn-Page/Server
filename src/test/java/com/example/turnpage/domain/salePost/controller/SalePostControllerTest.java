package com.example.turnpage.domain.salePost.controller;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostId;
import com.example.turnpage.domain.salePost.service.SalePostService;
import com.example.turnpage.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Book 컨트롤러의")
public class SalePostControllerTest extends ControllerTestConfig {
    @MockBean
    SalePostService salePostService;

    @Test
    @DisplayName("판매글 정보 저장 테스트")
    public void saveSalePostTest() throws Exception {

        //given
        final String url = "/salePosts";
        SaveBookRequest bookInfo = SaveBookRequest.builder()
                .itemId(1L)
                .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                .author("브로디, 노아")
                .cover("https://image.aladin.co.kr/product/33948/74/coversum/k392930236_1.jpg")
                .isbn("12987349382")
                .publisher("포레스트북스")
                .publicationDate("2023-12-18")
                .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                .build();

        SaveSalePostRequest request = SaveSalePostRequest.builder()
                .title("제목")
                .description("설명")
                .grade("최상")
                .price(10000)
                .bookInfo(bookInfo)
                .build();

        SalePostId response = new SalePostId(1L);

        given(salePostService.saveSalePost(any(Member.class), any(SaveSalePostRequest.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SS001"))
                .andExpect(jsonPath("$.data.salePostId").value(1))
        ;

        verify(salePostService).saveSalePost(any(Member.class), any(SaveSalePostRequest.class));
    }

    @Test
    @DisplayName("판매글 정보 수정 테스트")
    public void editSalePostTest() throws Exception {

        //given
        final String url = "/salePosts";

        EditSalePostRequest request = EditSalePostRequest.builder()
                .title("수정제목")
                .description("수정설명")
                .grade("상")
                .price(20000)
                .build();

        SalePostId response = new SalePostId(1L);

        given(salePostService.editSalePost(any(Member.class), any(EditSalePost.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(url)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SS002"))
                .andExpect(jsonPath("$.data.salePostId").value(1))
        ;

        verify(salePostService).saveSalePost(any(Member.class), any(EditSalePostRequest.class));
    }
}
