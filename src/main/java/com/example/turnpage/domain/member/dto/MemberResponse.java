package com.example.turnpage.domain.member.dto;

import lombok.*;

public class MemberResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MyPageInfo {
        private String name;
        private String email;
        private String profileImage;
        private String inviteCode;
        private int point;
        private int reportCount;
        private int saleCount;
        private int purchaseCount;
    }
}