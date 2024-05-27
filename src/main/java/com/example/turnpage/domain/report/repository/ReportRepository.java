package com.example.turnpage.domain.report.repository;

import com.example.turnpage.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByMemberId(Long memberId);
}
