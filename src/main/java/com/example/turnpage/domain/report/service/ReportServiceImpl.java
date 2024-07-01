package com.example.turnpage.domain.report.service;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.domain.follow.service.FollowService;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.domain.report.converter.ReportConverter;
import com.example.turnpage.domain.report.dto.ReportRequest;
import com.example.turnpage.domain.report.dto.ReportRequest.EditReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.DetailedReportInfo;
import com.example.turnpage.domain.report.dto.ReportResponse.PagedReportInfo;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.entity.Report;
import com.example.turnpage.domain.report.repository.ReportRepository;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.turnpage.global.error.code.ReportErrorCode.REPORT_NOT_FOUND;
import static com.example.turnpage.global.error.code.ReportErrorCode.WRITER_ONLY_MODIFY_REPORT;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportConverter reportConverter;
    private final FollowService followService;
    private final BookService bookService;
    private final MemberService memberService;


    @Transactional
    @Override
    public ReportId postReport(Member loginMember, ReportRequest.PostReportRequest request) {
        Member member = memberService.findMember(loginMember.getId());
        Book book = bookService.findBookByItemId(request.getBookInfo().getItemId())
                .orElseGet(() -> bookService.saveBookInfo(request.getBookInfo()));

        Report report = reportConverter.toEntity(request, book, member);
        Long reportId = reportRepository.save(report).getId();

        member.incrementReportCount();

        return reportConverter.toReportId(reportId);
    }

    @Override
    public PagedReportInfo findMyReportList(Member member, Pageable pageable) {
        Page<Report> reports = reportRepository.findByMemberId(member.getId(), pageable);

        return reportConverter.toPagedReportInfo(reports);
    }

    @Override
    public PagedReportInfo findReportListOfFollowingMembers(Member member, Pageable pageable) {
        List<Long> followingIdList = followService.getFollowingList(member)
                .stream()
                .map(memberInfo -> memberInfo.getMemberId())
                .toList();

        Page<Report> reports = reportRepository.findByMemberIdInOrderByCreatedAtDesc(followingIdList, pageable);
        return reportConverter.toPagedReportInfo(reports);
    }

    @Override
    public PagedReportInfo searchReportList(Member member, String keyword, Pageable pageable) {
        List<Long> followingIdList = followService.getFollowingList(member)
                .stream()
                .map(memberInfo -> memberInfo.getMemberId())
                .toList();

        Page<Report> reportList = reportRepository.searchByTitleOrBookOrWriter(followingIdList, keyword, pageable);
        return reportConverter.toPagedReportInfo(reportList);
    }

    @Override
    public DetailedReportInfo getReport(Member member, Long reportId) {
        Report report = findReport(reportId);

        boolean isMine = checkIsMine(report, member);
        return reportConverter.toDetailedReportInfo(report, isMine);
    }

    @Transactional
    @Override
    public ReportId editReport(Member member, Long reportId, EditReportRequest request) {
        Report report = findReport(reportId);

        validateWriter(report, member);
        report.edit(request);
        return reportConverter.toReportId(report.getId());
    }

    @Transactional
    @Override
    public ReportId deleteReport(Member loginMember, Long reportId) {
        Member member = memberService.findMember(loginMember.getId());
        Report report = findReport(reportId);

        validateWriter(report, member);

        report.delete();
        member.decrementReportCount();

        return reportConverter.toReportId(reportId);
    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(REPORT_NOT_FOUND));
    }

    private boolean checkIsMine(Report report, Member viewer) {
        Long writerId = report.getMember().getId();
        Long viewerId = viewer.getId();

        return writerId.equals(viewerId);
    }

    private void validateWriter(Report report, Member member) {
        Long writerId = report.getMember().getId();
        Long memberId = member.getId();

        if (!writerId.equals(memberId)) {
            throw new BusinessException(WRITER_ONLY_MODIFY_REPORT);
        }
    }
}
