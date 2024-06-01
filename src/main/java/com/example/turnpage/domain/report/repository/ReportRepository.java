package com.example.turnpage.domain.report.repository;

import com.example.turnpage.domain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long reportId);
    Page<Report> findByMemberId(Long memberId, Pageable pageable);
    Page<Report> findByMemberIdInOrderByCreatedAtDesc(List<Long> followingIdList, Pageable pageable);
}
