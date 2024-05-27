package com.example.turnpage.domain.salePost.service;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.SalePostErrorCode;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Sale Post Service 의")
public class SalePostServiceTest extends ServiceTestConfig {

    @Autowired
    SalePostService salePostService;

    @Test
    @Transactional
    @DisplayName("판매글 저장 성공 테스트")
    public void saveSalePost() {
        //given
        SaveBookRequest bookRequest = SaveBookRequest.builder()
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
                 .bookInfo(bookRequest)
                 .build();
        //when
        SalePost salePost = salePostService.findSalePost(salePostService.saveSalePost(member, request).getSalePostId());

        //then
        assertEquals(salePost.getTitle(), "제목");
        assertEquals(salePost.getDescription(), "설명");
        assertEquals(salePost.getPrice(), 10000);
        assertEquals(salePost.getGrade(), Grade.MINT);
        assertEquals(salePost.getBook().getItemId(),1);
        assertEquals(salePost.getMember().getName(),"수밈");
    }

    @Test
    @Transactional
    @DisplayName("판매글 저장 실패 테스트 - GRADE ENUM을 찾을 수 없음")
    public void saveSalePostFail_InvalidGrade() {
        //given
        SaveBookRequest bookRequest = SaveBookRequest.builder()
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
                .grade("최상상")
                .price(10000)
                .bookInfo(bookRequest)
                .build();
        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            salePostService.saveSalePost(member, request);
        });

        //then
        assertEquals(SalePostErrorCode.INVALID_GRADE_INPUT.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @Transactional
    @DisplayName("판매글 수정 성공 테스트")
    public void saveSalePost() {
        //given
        EditSalePostRequest request = EditSalePostRequest.builder()
                .title("수정제목")
                .description("수정설명")
                .grade("상")
                .price(20000)
                .build();
        //when
        SalePost salePost = salePostService.findSalePost(salePostService.editSalePost(member, request).getSalePostId());

        //then
        assertEquals(salePost.getTitle(), "수정제목");
        assertEquals(salePost.getDescription(), "수정설명");
        assertEquals(salePost.getPrice(), 20000);
        assertEquals(salePost.getGrade(), Grade.TOP);
        assertEquals(salePost.getBook().getItemId(),1);
        assertEquals(salePost.getMember().getName(),"수밈");
    }
}
