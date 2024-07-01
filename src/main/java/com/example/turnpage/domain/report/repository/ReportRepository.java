package com.example.turnpage.domain.report.repository;

import com.example.turnpage.domain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long reportId);
    Page<Report> findByMemberId(Long memberId, Pageable pageable);
    Page<Report> findByMemberIdInOrderByCreatedAtDesc(List<Long> followingIdList, Pageable pageable);

    @Query("SELECT r FROM Report AS r "
            + "JOIN FETCH r.book "
            + "JOIN FETCH r.member "
            + "WHERE r.member.id IN :followingIdList "
            + "AND ( "
            + "REPLACE(r.title, ' ', '')  LIKE %:keyword% "
            + "OR REPLACE(r.book.title, ' ', '') LIKE %:keyword% "
            + "OR REPLACE(r.book.author, ' ', '') LIKE %:keyword% "
            + "OR REPLACE(r.member.name, ' ', '') LIKE %:keyword% ) "
            + "ORDER BY r.createdAt DESC ")
    Page<Report> searchByTitleOrBookOrWriter(List<Long> followingIdList, String keyword, Pageable pageable);
}
