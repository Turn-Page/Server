package com.example.turnpage.domain.report.service;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.domain.friend.entity.Friend;
import com.example.turnpage.domain.friend.repository.FriendRepository;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.converter.ReportConverter;
import com.example.turnpage.domain.report.dto.ReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;
import com.example.turnpage.domain.report.entity.Report;
import com.example.turnpage.domain.report.repository.ReportRepository;
import com.example.turnpage.global.entity.BaseTimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final FriendRepository friendRepository;
    private final ReportConverter reportConverter;
    private final BookService bookService;


    // 리뷰 포인트: book 도메인 단의 Service단과 Repository단 중 뭘 써야 할까
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
        // sender가 타겟 회원, receiver가 타겟 회원과 친구인 회원이라고 가정한다.
        List<Member> friends = friendRepository.findBySenderId(member.getId())
                .stream()
                .map(friend -> friend.getReceiver())
                .collect(Collectors.toList());


        // 최근 작성날짜 순으로 보여주는 createdAt DESC 정렬 로직 필요함
        // 현재는 BaseEntity가 Comparable을 구현하도록 해놔서 stream API를 이용해 단순 날짜 오름차순으로 리턴함
        List<Report> reportList = friends
                .stream()
                .map(friend -> reportRepository.findByMemberId(friend.getId()))
                .flatMap(reports -> reports.stream())
                .sorted()
                .collect(Collectors.toList());

        // 날짜 오름차순인 독후감 목록을 reverse
        Collections.reverse(reportList);

        return reportConverter.toReportInfoList(reportList);
    }
}
