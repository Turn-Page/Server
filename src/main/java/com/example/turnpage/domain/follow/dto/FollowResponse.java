package com.example.turnpage.domain.follow.dto;

import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class FollowResponse {
    @Getter
    @AllArgsConstructor
    public static class FollowId {
        private Long followId;
    }

    @Getter
    @AllArgsConstructor
    public static class FollowInfo {
        private Long followId;
        private MemberInfo memberInfo;
    }
}
