package com.example.turnpage.domain.salePost.repository;

import com.example.turnpage.domain.salePost.entity.SalePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalePostRepository extends JpaRepository<SalePost,Long> {
    @Query("SELECT sp FROM SalePost sp JOIN FETCH sp.book ORDER BY sp.createdAt DESC")
    Page<SalePost> findSalePostsWithBooksOrderByCreatedAt(Pageable pageable);
}
