package com.example.turnpage.domain.salePost.converter;

import com.example.turnpage.domain.book.converter.BookConverter;
import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostResponse;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostInfo;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.domain.salePost.entity.SalePost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SalePostConverter {
    private final BookConverter bookConverter;
    public SalePost toEntity(Member member, Book book, String title, String description, Grade grade, Integer price) {
        return SalePost.builder()
                .member(member)
                .book(book)
                .title(title)
                .description(description)
                .grade(grade)
                .price(price)
                .build();
    }

    public SalePostInfo toSalePostInfo(SalePost salePost) {
        return SalePostInfo.builder()
                .bookInfo(bookConverter.tokBookInfo(salePost.getBook()))
                .title(salePost.getTitle())
                .salePostId(salePost.getId())
                .price(salePost.getPrice())
                .grade(salePost.getGrade().getToKorean())
                .createdAt(salePost.getCreatedAt())
                .build();
    }
    public SalePostResponse.PagedSalePostList toPagedSalePostList(Page<SalePost> salePosts) {
        List<SalePostInfo> salePostList = salePosts.stream()
                .map(this::toSalePostInfo).toList();

        return SalePostResponse.PagedSalePostList.builder()
                .salePostList(salePostList)
                .page(salePosts.getNumber())
                .totalPages(salePosts.getTotalPages())
                .totalElements((int) salePosts.getTotalElements())
                .isFirst(salePosts.isFirst())
                .isLast(salePosts.isLast())
                .build();
    }
}
