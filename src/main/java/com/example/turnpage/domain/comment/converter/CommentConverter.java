package com.example.turnpage.domain.comment.converter;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.comment.dto.CommentRequest.WriteCommentRequest;
import com.example.turnpage.domain.comment.dto.CommentResponse.CommentId;
import com.example.turnpage.domain.comment.dto.CommentResponse.CommentInfo;
import com.example.turnpage.domain.comment.dto.CommentResponse.PagedCommentInfo;
import com.example.turnpage.domain.comment.entity.Comment;
import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import com.example.turnpage.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final MemberConverter memberConverter;

    public Comment toEntity(Member member, Book book, WriteCommentRequest request) {
        return Comment.builder()
                .member(member)
                .book(book)
                .star(request.getStar())
                .comment(request.getComment())
                .build();
    }

    public CommentId toCommentId(Long commentId) {
        return new CommentId(commentId);
    }

    public CommentInfo toCommentInfo(Comment comment) {
        MemberInfo memberInfo = memberConverter.toMemberInfo(comment.getMember());

        return CommentInfo.builder()
                .memberInfo(memberInfo)
                .star(comment.getStar())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt().toLocalDate())
                .build();
    }

    public PagedCommentInfo toPagedCommentInfo(Page<Comment> comments) {
        List<CommentInfo> commentInfoList = comments.stream()
                .map(comment -> toCommentInfo(comment))
                .toList();

        return PagedCommentInfo.builder()
                .commentInfoList(commentInfoList)
                .page(comments.getNumber())
                .totalPages(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }
}
