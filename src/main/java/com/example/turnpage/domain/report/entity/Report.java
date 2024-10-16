package com.example.turnpage.domain.report.entity;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest.EditReportRequest;
import com.example.turnpage.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@SQLRestriction("deleted_at is NULL")
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false, length = 100000)
    private String content;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public void edit(EditReportRequest request) {
        editTitle(request.getTitle());
        editContent(request.getContent());
        editStartDate(request.getStartDate());
        editEndDate(request.getEndDate());
    }

    private void editTitle(String title) {
        if (!this.title.equals(title)) {
            this.title = title;
        }
    }

    private void editContent(String content) {
        if (!this.content.equals(content)) {
            this.content = content;
        }
    }

    private void editStartDate(LocalDate startDate) {
        if (!this.startDate.isEqual(startDate)) {
            this.startDate = startDate;
        }
    }

    private void editEndDate(LocalDate endDate) {
        if (!this.endDate.isEqual(endDate)) {
            this.endDate = endDate;
        }
    }
}
