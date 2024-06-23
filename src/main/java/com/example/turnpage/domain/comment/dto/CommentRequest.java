package com.example.turnpage.domain.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class CommentRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WriteCommentRequest {
        @NotNull
        private Long bookId;
        @Min(1) @Max(5)
        private int star;
        private String comment;
    }
}
