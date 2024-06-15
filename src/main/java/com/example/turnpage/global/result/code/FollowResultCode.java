package com.example.turnpage.global.result.code;

import com.example.turnpage.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowResultCode implements ResultCode {
    FOLLOW_MEMBER(200, "SF001", "성공적으로 해당 이메일을 가진 회원을 팔로우하였습니다."),
    FOLLOWING_FOLLOWER_LIST(200, "SF002", "성공적으로 회원의 팔로잉,팔로워 목록을 조회하였습니다."),
    UNFOLLOW_MEMBER(200, "SF003", "성공적으로 해당 memberId를 가진 회원을 언팔로우하였습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
