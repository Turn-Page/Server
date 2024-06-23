package com.example.turnpage.domain.comment.dto;

import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class CommentResponse {

    @Getter
    @AllArgsConstructor
    public static class CommentId {
        private Long commentId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class CommentInfo {
        private MemberInfo memberInfo;
        private int star;
        private String comment;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate createdAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PagedCommentInfo {
        @Builder.Default
        private List<CommentInfo> commentInfoList = new ArrayList<>();
        private int page;
        private int totalPages;
        private long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
