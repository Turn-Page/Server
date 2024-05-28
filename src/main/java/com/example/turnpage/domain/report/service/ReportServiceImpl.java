package com.example.turnpage.domain.report.service;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.domain.friend.service.FriendService;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.converter.ReportConverter;
import com.example.turnpage.domain.report.dto.ReportRequest;
import com.example.turnpage.domain.report.dto.ReportRequest.EditReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;
import com.example.turnpage.domain.report.entity.Report;
import com.example.turnpage.domain.report.repository.ReportRepository;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.turnpage.global.error.domain.ReportErrorCode.REPORT_NOT_FOUND;
import static com.example.turnpage.global.error.domain.ReportErrorCode.WRITER_ONLY_MODIFY_REPORT;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportConverter reportConverter;
    private final FriendService friendService;
    private final BookService bookService;


    @Transactional
    @Override
    public ReportId postReport(Member member, ReportRequest.PostReportRequest request) {
        Long bookId = bookService.saveBook(request.getBookInfo())
                .getBookId();

        Book book = bookService.findBook(bookId);
        Report report = reportConverter.toEntity(request, book, member);
        Long reportId = reportRepository.save(report).getId();

        return reportConverter.toReportId(reportId);
    }

    @Override
    public List<ReportInfo> findMyReportList(Member member) {
        List<Report> reportList = reportRepository.findByMemberId(member.getId());

        return reportConverter.toReportInfoList(reportList);
    }

    @Override
    public List<ReportInfo> findFriendsReportList(Member member) {
        List<Long> friendIdList = friendService.getFriendList(member)
                .stream()
                .map(friend -> friend.getMemberId())
                .collect(Collectors.toList());

        List<Report> reportList = reportRepository.findByMemberIdInOrderByCreatedAtDesc(friendIdList);

        return reportConverter.toReportInfoList(reportList);
    }

    @Override
    public ReportInfo getReport(Member member, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(REPORT_NOT_FOUND));

        return reportConverter.toReportInfo(report);
    }

    @Transactional
    @Override
    public ReportId editReport(Member member, Long reportId, EditReportRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(REPORT_NOT_FOUND));

        validateWriter(report, member);
        report.edit(request);
        reportRepository.flush();

        return reportConverter.toReportId(report.getId());
    }

    @Transactional
    @Override
    public ReportId deleteReport(Member member, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(REPORT_NOT_FOUND));

        validateWriter(report, member);
        reportRepository.delete(report);
        reportRepository.flush();
        return reportConverter.toReportId(reportId);
    }

    private void validateWriter(Report report, Member member) {
        if (!report.getMember().equals(member)) {
            throw new BusinessException(WRITER_ONLY_MODIFY_REPORT);
        }
    }
}
