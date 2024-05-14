package com.example.turnpage.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditMyPageRequest {
        private String name;
    }
}
