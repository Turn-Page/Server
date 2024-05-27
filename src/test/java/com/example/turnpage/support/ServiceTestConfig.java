package com.example.turnpage.support;

import com.example.turnpage.domain.book.dto.BookRequest;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.repository.BookRepository;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.domain.salePost.dto.SalePostRequest;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.domain.salePost.repository.SalePostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class ServiceTestConfig {
    @Autowired protected MemberRepository memberRepository;
    @Autowired protected BookRepository bookRepository;
    @Autowired protected SalePostRepository salePostRepository;
    protected Member testMember;
    protected Book testBook;
    protected SalePost testSalePost;

    @BeforeEach
    public void setUp() {
        testMember = createMember();
        testBook = createBook();
        testSalePost = createSalePost();
    }

    protected Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .id(100L)
                        .name("수밈")
                        .email("sumin@gmail.com")
                        .inviteCode("aaaaaa")
                        .image("http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
                        .role(Role.USER)
                        .socialType(SocialType.GOOGLE)
                        .point(0)
                        .reportCount(0)
                        .purchaseCount(0)
                        .saleCount(0)
                        .build()
        );
    }

    protected Book createBook() {
        return bookRepository.save(
                Book.builder()
                        .id(100L)
                        .itemId(1L)
                        .title("꿈꾸지 않아도 빤짝이는 중 - 놀면서 일하는 두 남자 삐까뚱씨, 내일의 목표보단 오늘의 행복에 집중하는 인생로그")
                        .author("브로디, 노아")
                        .cover("https://image.aladin.co.kr/product/33948/74/coversum/k392930236_1.jpg")
                        .isbn("12987349382")
                        .publisher("포레스트북스")
                        .publicationDate("2023-12-18")
                        .description("삐까뚱씨라는 이름으로 유튜브를 하고 있는 브로디와 노아.")
                        .ranking(null)
                        .star(0.0)
                        .build()
        );
    }

    protected SalePost createSalePost() {
        return salePostRepository.save(
                SalePost.builder()
                        .id(100L)
                        .title("제목")
                        .description("설명")
                        .grade(Grade.MINT)
                        .price(10000)
                        .member(testMember)
                        .book(testBook)
                        .build()
        );
    }
}
