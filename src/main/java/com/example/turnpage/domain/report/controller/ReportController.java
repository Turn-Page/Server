package com.example.turnpage.domain.report.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.report.dto.ReportRequest;
import com.example.turnpage.domain.report.dto.ReportRequest.PostReportRequest;
import com.example.turnpage.domain.report.dto.ReportResponse.PagedReportList;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportId;
import com.example.turnpage.domain.report.dto.ReportResponse.ReportInfo;
import com.example.turnpage.domain.report.service.ReportService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.global.result.code.ReportResultcode.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping()
    @Operation(summary = "새 독후감 등록 API", description = "새 독후감 등록 API입니다.")
    public ResultResponse<ReportId> postReport(@LoginMember Member member,
                                               @Valid @RequestBody PostReportRequest request) {
        return ResultResponse.of(POST_REPORT, reportService.postReport(member, request));
    }

    @GetMapping("/my")
    @Operation(summary = "내 독후감 목록 조회 API", description = "내 독후감 목록 조회 API입니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 독후감 개수를 입력해주세요.")
    })
    public ResultResponse<PagedReportList> findMyReportList(@LoginMember Member member,
                                                            @PageableDefault(sort = "createdAt",
                                                                     direction = Sort.Direction.DESC)
                                                             Pageable pageable) {
        return ResultResponse.of(MY_REPORT_LIST, reportService.findMyReportList(member, pageable));
    }

    @GetMapping("/friends")
    @Operation(summary = "친구들의 목록 조회 API", description = "친구들의 독후감 목록 조회 API입니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 독후감 개수를 입력해주세요.")
    })
    ResultResponse<PagedReportList> findFriendsReportList(@LoginMember Member member,
                                                           @PageableDefault(sort = "createdAt",
                                                                   direction = Sort.Direction.DESC)
                                                           Pageable pageable) {
        return ResultResponse.of(FRIENDS_REPORT_LIST, reportService.findFriendsReportList(member, pageable));
    }

    @GetMapping("/{reportId}")
    @Parameters(value = {
            @Parameter(name = "reportId", description = "조회하고자 하는 독후감의 reportId를 입력해 주세요.")
    })
    @Operation(summary = "특정 독후감 조회 API", description = "reportId를 통한 특정 독후감 조회 API입니다.")
    ResultResponse<ReportInfo> getReport(@LoginMember Member member,
                                         @PathVariable("reportId") Long reportId) {
        return ResultResponse.of(GET_SPECIFIC_REPORT, reportService.getReport(member, reportId));
    }

    @PatchMapping("/{reportId}")
    @Parameters(value = {
            @Parameter(name = "reportId", description = "수정하고자 하는 독후감의 reportId를 입력해 주세요.")
    })
    @Operation(summary = "특정 독후감 수정 API", description = "reportId를 통한 특정 독후감 수정 API입니다.")
    ResultResponse<ReportId> editReport(@LoginMember Member member,
                                        @PathVariable("reportId") Long reportId,
                                        @Valid @RequestBody ReportRequest.EditReportRequest request) {
        return ResultResponse.of(UPDATE_SPECIFIC_REPORT, reportService.editReport(member, reportId, request));
    }

    @DeleteMapping("/{reportId}")
    @Parameters(value = {
            @Parameter(name = "reportId", description = "삭제하고자 하는 독후감의 reportId를 입력해 주세요.")
    })
    @Operation(summary = "특정 독후감 삭제 API", description = "reportId를 통한 특정 독후감 삭제 API입니다.")
    ResultResponse<ReportId> deleteReport(@LoginMember Member member,
                                          @PathVariable("reportId") Long reportId) {
        return ResultResponse.of(DELETE_SPECIFIC_REPORT, reportService.deleteReport(member, reportId));
    }
}
