package com.example.turnpage.domain.comment.service;

import com.example.turnpage.domain.comment.dto.CommentRequest.WriteCommentRequest;
import com.example.turnpage.domain.comment.dto.CommentResponse.CommentId;
import com.example.turnpage.domain.comment.dto.CommentResponse.PagedCommentInfo;
import com.example.turnpage.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentId writeComment(Member member, WriteCommentRequest request);
    PagedCommentInfo findCommentListByBookId(Long bookId, Pageable pageable);
    CommentId deleteComment(Member member, Long commentId);
}
