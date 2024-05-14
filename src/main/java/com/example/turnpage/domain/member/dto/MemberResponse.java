package com.example.turnpage.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberId {
        private Long memberId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyPoint {
        private Long memberId;
        private int totalPoint;
    }
}