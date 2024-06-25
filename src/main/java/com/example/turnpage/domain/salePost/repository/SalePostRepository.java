package com.example.turnpage.domain.salePost.repository;

import com.example.turnpage.domain.salePost.entity.SalePost;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SalePostRepository extends JpaRepository<SalePost, Long> {
    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.book " +
            "WHERE (:total = true OR sp.isSold = false) " +
            "ORDER BY sp.createdAt DESC ")
    Page<SalePost> findSalePostsWithBooksOrderByCreatedAt(@Param("total") boolean total, Pageable pageable);

    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.book WHERE REPLACE(sp.title,' ','') LIKE %:keyword% " +
            "OR REPLACE(sp.book.title,' ','') LIKE %:keyword% " +
            "OR REPLACE(sp.book.author,' ','') LIKE %:keyword% " +
            "AND (:total = true OR sp.isSold = false)" +
            "ORDER BY sp.createdAt DESC")
    Page<SalePost> findByBookOrTitleContaining(@Param("total") boolean total, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.member JOIN FETCH sp.book WHERE sp.id = :salePostId")
    Optional<SalePost> findSalePostWithMemberAndBook(@Param("salePostId") Long salePostId);

    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.book WHERE sp.member.id = :memberId" +
            " AND (:total = true OR sp.isSold = false) ORDER BY sp.createdAt DESC")
    Page<SalePost> findByMemberId(@Param("memberId") Long memberId, @Param("total") boolean total, Pageable pageable);
}
