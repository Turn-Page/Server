package com.example.turnpage.domain.salePost.repository;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.salePost.entity.SalePost;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SalePostRepository extends JpaRepository<SalePost,Long> {
    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.book")
    Page<SalePost> findSalePostsWithBooksOrderByCreatedAt(Pageable pageable);

    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.book WHERE REPLACE(sp.title,' ','') LIKE %:keyword% " +
            "OR REPLACE(sp.book.title,' ','') LIKE %:keyword% " +
            "OR REPLACE(sp.book.author,' ','') LIKE %:keyword% ")
    Page<SalePost> findByBookOrTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.member JOIN FETCH sp.book WHERE sp.id = :salePostId")
    Optional<SalePost> findSalePostWithMemberAndBook(@Param("salePostId") Long salePostId);
}
