package com.example.turnpage.domain.report.repository;

import com.example.turnpage.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long reportId);
    List<Report> findByMemberId(Long memberId);
    List<Report> findByMemberIdInOrderByCreatedAtDesc(List<Long> friendIdList);
}