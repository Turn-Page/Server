package com.example.turnpage.domain.follow.controller;

import com.example.turnpage.domain.follow.dto.FollowRequest.FollowMemberRequest;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowId;
import com.example.turnpage.domain.follow.service.FollowService;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.turnpage.global.result.code.FollowResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
@Tag(name = "팔로우 API", description = "팔로우 관련 API입니다.")
public class FollowController {
    private final FollowService followService;

    @PostMapping
    @Operation(summary = "회원 팔로우 API", description = "특정 회원을 팔로우합니다.")
    public ResultResponse<FollowId> followMember(@LoginMember Member member,
                                               @RequestBody @Valid FollowMemberRequest request) {
        return ResultResponse.of(FOLLOW_MEMBER, followService.followMember(member, request));
    }

    @GetMapping
    @Operation(summary = "팔로잉 목록 조회 API", description = "회원이 팔로우하고 있는 회원의 목록을 조회합니다.")
    public ResultResponse<List<MemberInfo>> getFollowingList(@LoginMember Member member) {
        return ResultResponse.of(FOLLOWING_LIST, followService.getFollowingList(member));
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "회원 언팔로우 API", description = "회원이 팔로우하고 있던 회원을 언팔로우합니다.")
    public ResultResponse<FollowId> unfollowMember(@LoginMember Member member,
                                                   @PathVariable("memberId") Long memberId) {
        return ResultResponse.of(UNFOLLOW_MEMBER, followService.unfollowMember(member, memberId));
    }
}
