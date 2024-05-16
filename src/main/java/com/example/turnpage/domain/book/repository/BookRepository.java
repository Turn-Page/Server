package com.example.turnpage.domain.book.repository;

import com.example.turnpage.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByRankingNotNullOrderByRanking(Pageable pageable);
}
