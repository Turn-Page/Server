package com.example.turnpage.domain.comment.controller;

import com.example.turnpage.domain.comment.dto.CommentRequest.WriteCommentRequest;
import com.example.turnpage.domain.comment.dto.CommentResponse.PagedCommentInfo;
import com.example.turnpage.domain.comment.service.CommentService;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.global.result.code.CommentResultCode.*;

@RequestMapping("/comments")
@Tag(name = "도서 리뷰 API", description = "도서 리뷰 관련 API입니다.")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Parameters(value = {
            @Parameter(name = "bookId", description = "리뷰를 작성할 도서의 bookId를 입력해 주세요.")
    })
    @Operation(summary = "새 도서 리뷰 작성 API", description = "특정 도서에 대하여 새 리뷰를 작성합니다.")
    public ResultResponse<Object> writeComment(@LoginMember Member member,
                                               @Valid @RequestBody WriteCommentRequest request) {
        return ResultResponse.of(WRITE_COMMENT, commentService.writeComment(member, request));
    }

    @GetMapping
    @Parameters(value = {
            @Parameter(name = "bookId", description = "리뷰를 조회할 도서의 bookId를 입력해 주세요."),
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 comment 개수를 입력해 주세요.")
    })
    @Operation(summary = "특정 도서의 리뷰 목록 조회 API", description = "특정 도서에 대하여 새 리뷰를 작성합니다.")
    public ResultResponse<PagedCommentInfo> findCommentListByBookId(@RequestParam("bookId") Long bookId,
                                                                    @PageableDefault(sort = "createdAt",
                                                                            direction = Sort.Direction.DESC)
                                                                    @Parameter(hidden = true)
                                                                    Pageable pageable) {
        return ResultResponse.of(SPECIFIC_BOOK_COMMENT_LIST, commentService.findCommentListByBookId(bookId, pageable));
    }

    @DeleteMapping("/{commentId}")
    @Parameters(value = {
            @Parameter(name = "commentId", description = "삭제할 리뷰의 commentId를 입력해 주세요.")
    })
    @Operation(summary = "도서 리뷰 삭제 API", description = "특정 리뷰를 삭제합니다.")
    public ResultResponse<Object> deleteComment(@LoginMember Member member,
                                                @PathVariable("commentId") Long commentId) {
        return ResultResponse.of(DELETE_COMMENT, commentService.deleteComment(member, commentId));
    }
}
