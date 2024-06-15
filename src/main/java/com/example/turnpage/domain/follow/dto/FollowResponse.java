package com.example.turnpage.domain.follow.dto;

import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class FollowResponse {
    @Getter
    @AllArgsConstructor
    public static class FollowId {
        private Long followId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowInfo {
        private Long followId;
        private MemberInfo memberInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowingFollowerList {
        private List<FollowInfo> followingInfoList;
        private List<FollowInfo> followerInfoList;
        private Integer followingCount;
        private Integer followerCount;
    }

}
