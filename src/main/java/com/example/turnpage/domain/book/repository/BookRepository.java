package com.example.turnpage.domain.book.repository;

import com.example.turnpage.domain.book.entity.Book;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByRankingNotNullOrderByRanking(Pageable pageable);

    //띄어쓰기 무시하고 검색하기
     @Query("SELECT b FROM Book b WHERE REPLACE(b.title, ' ', '') LIKE %:keyword% OR REPLACE(b.author, ' ', '') LIKE %:keyword%")
    Page<Book> findByTitleOrAuthorContaining(@Param("keyword") String keyword, Pageable pageable);
    @Modifying
    @Query("UPDATE Book b SET b.ranking = null WHERE b.ranking IS NOT NULL")
    void updateRankToNull();

    @Query("SELECT b.itemId FROM Book b WHERE b.ranking IS NOT NULL")
    List<Long> findAllItemIdByRankingNotNull();

    Optional<Book> findByItemId(Long itemId);
}
