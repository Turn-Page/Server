package com.example.turnpage.domain.salePost.service;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.EditSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostDetailInfo;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.code.SalePostErrorCode;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import static com.example.turnpage.domain.salePost.dto.SalePostResponse.PagedSalePostInfo;
import static org.junit.jupiter.api.Assertions.*;

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
        SalePost salePost = salePostService.findSalePost(salePostService.saveSalePost(testMember, request).getSalePostId());

        //then
        assertEquals("제목", salePost.getTitle());
        assertEquals("설명", salePost.getDescription());
        assertEquals(10000, salePost.getPrice());
        assertEquals(Grade.MINT, salePost.getGrade());
        assertEquals(1, salePost.getBook().getItemId());
        assertEquals("수밈", salePost.getMember().getName());
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
            salePostService.saveSalePost(testMember, request);
        });

        //then
        assertEquals(SalePostErrorCode.INVALID_GRADE_INPUT, exception.getErrorCode());
    }


    @Test
    @Transactional
    @DisplayName("판매글 수정 실패 테스트 - 판매 작성자가 로그인 멤버가 아님")
    public void editSalePostFail() {
        //given
        EditSalePostRequest request = EditSalePostRequest.builder()
                .title("수정제목")
                .description("수정설명")
                .grade("상")
                .price(20000)
                .build();

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            salePostService.editSalePost(testMember2, testSalePost.getId(), request);
        });

        //then
        assertEquals(SalePostErrorCode.NO_AUTHORIZATION_SALE_POST, exception.getErrorCode());
    }
    @Test
    @Transactional
    @DisplayName("판매글 수정 성공 테스트")
    public void editSalePost() {
        //given
        EditSalePostRequest request = EditSalePostRequest.builder()
                .title("수정제목")
                .description("수정설명")
                .grade("상")
                .price(20000)
                .build();

        //when
        SalePost salePost = salePostService.findSalePost(salePostService.editSalePost(testMember, testSalePost.getId(), request).getSalePostId());

        //then
        assertEquals("수정제목", salePost.getTitle());
        assertEquals("수정설명", salePost.getDescription());
        assertEquals(20000, salePost.getPrice());
        assertEquals(Grade.TOP, salePost.getGrade());
        assertEquals(1, salePost.getBook().getItemId());
        assertEquals("수밈", salePost.getMember().getName());
        assertNotNull(salePost.getUpdatedAt());
    }


    @Test
    @Transactional
    @DisplayName("판매글 삭제 실패 테스트 - 판매 완료된 게시글을 삭제할 수 없음")
    public void deleteSalePostFail() {

        //given
        testSalePost.setSold();

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            salePostService.deleteSalePost(testMember, testSalePost.getId());
        });

        //then
        assertEquals(SalePostErrorCode.SALE_POST_NOT_ALLOWED, exception.getErrorCode());
    }


    @Test
    @Transactional
    @DisplayName("판매글 삭제 성공 테스트")
    public void deleteSalePost() {
        //given //when
        salePostService.deleteSalePost(testMember, testSalePost.getId());
        //then
        assertNotNull(testSalePost.getDeletedAt());
    }

    @Test
    @Transactional
    @DisplayName("판매글 목록 조회 성공 테스트")
    public void fetchSalePost() {
        //given
        for(int i=1;i<=10;i++) {
            SaveBookRequest bookRequest = SaveBookRequest.builder()
                    .itemId((long) i)
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

            salePostService.saveSalePost(testMember, request);
        }
        //when
        testSalePost.setSold();
        Pageable pageable = PageRequest.of(0, 20);
        PagedSalePostInfo salePostList = salePostService.fetchSalePosts(true, pageable);


        //then
        assertEquals(11, salePostList.getTotalElements());
        assertEquals(0, salePostList.getPage());
        assertEquals(1, salePostList.getTotalPages());
        assertEquals("제목",salePostList.getSalePostInfoList().get(0).getTitle());
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그",
                salePostList.getSalePostInfoList().get(0).getBookInfo().getTitle());
    }

    @Test
    @Transactional
    @DisplayName("판매글 목록 조회 성공 테스트 - 판매중인 게시글만 보기")
    public void fetchSalePost2() {
        //given
        for(int i=1;i<=10;i++) {
            SaveBookRequest bookRequest = SaveBookRequest.builder()
                    .itemId((long) i)
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

            salePostService.saveSalePost(testMember, request);
        }
        //when
        testSalePost.setSold();
        Pageable pageable = PageRequest.of(0, 20);
        PagedSalePostInfo salePostList = salePostService.fetchSalePosts(false, pageable);

        //then
        assertEquals(10, salePostList.getTotalElements());
        assertEquals(0, salePostList.getPage());
        assertEquals(1, salePostList.getTotalPages());
        assertEquals("제목",salePostList.getSalePostInfoList().get(0).getTitle());
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그",
                salePostList.getSalePostInfoList().get(0).getBookInfo().getTitle());
    }

    @Test
    @Transactional
    @DisplayName("판매글 검색 성공 테스트")
    public void searchSalePost() {

        //given & when
        Pageable pageable = PageRequest.of(0, 20);
        PagedSalePostInfo salePostList = salePostService.searchSalePost(false,"제목",pageable);

        assertEquals(0, salePostList.getPage());
        assertEquals(1,salePostList.getTotalPages());
        assertEquals(1, salePostList.getTotalElements());
        assertEquals("최상", salePostList.getSalePostInfoList().get(0).getGrade());
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그",
                salePostList.getSalePostInfoList().get(0).getBookInfo().getTitle());
    }

    @Test
    @Transactional
    @DisplayName("판매글 상세 조회 실패 테스트 - salePostId를 찾을 수 없음")
    public void getSalePostDetail() {

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            salePostService.getSalePostDetailInfo(testMember,1001L);
        });

        //then
        assertEquals(SalePostErrorCode.SALE_POST_NOT_FOUND, exception.getErrorCode());

    }


    @Test
    @Transactional
    @DisplayName("판매글 상세 조회 성공 테스트 - 로그인 했을 경우")
    public void getSalePostDetailFail() {

        //given & when
        SalePostDetailInfo detailInfo = salePostService.getSalePostDetailInfo(testMember,testSalePost.getId());

        //then
        assertEquals("제목", detailInfo.getTitle());
        assertEquals("설명", detailInfo.getDescription());
        assertEquals(10000, detailInfo.getPrice());
        assertEquals("최상", detailInfo.getGrade());
        assertEquals("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그",
                detailInfo.getBookInfo().getTitle());
        assertEquals("수밈", detailInfo.getMemberInfo().getName());
        assertEquals(true, detailInfo.isMine());
    }

    @Test
    @Transactional
    @DisplayName("판매글 상세 조회 성공 테스트2 - 로그인 하지 않았을 경우")
    public void getSalePostDetail2() {

        //given & when
        SalePostDetailInfo detailInfo = salePostService.getSalePostDetailInfo(null,testSalePost.getId());

        //then
        assertEquals(false, detailInfo.isMine());
    }



}
