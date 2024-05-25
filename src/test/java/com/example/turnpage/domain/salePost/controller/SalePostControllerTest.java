package com.example.turnpage.domain.salePost.controller;

import com.example.turnpage.domain.book.dto.BookRequest;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
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

        SaveSalePostRequest request = SaveSalePostRequest.builder()
                .title("제목")
                .description("설명")
                .grade(Grade.MINT)
                .price(10000)
                .bookInfo(any(BookRequest.SaveBookRequest.class))
                .build();

        SalePostResponse.SalePostId response = new SalePostResponse.SalePostId(1L);

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
                .andExpect(jsonPath("$.data.salePostId").value("1"))
        ;

        verify(salePostService.saveSalePost(any(Member.class), any(SaveSalePostRequest.class)));
    }
}
