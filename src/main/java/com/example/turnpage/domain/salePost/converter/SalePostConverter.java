package com.example.turnpage.domain.salePost.converter;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostResponse;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostId;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.domain.salePost.entity.SalePost;
import org.springframework.stereotype.Component;

@Component
public class SalePostConverter {
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
}
