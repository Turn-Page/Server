package com.example.turnpage.domain.follow.service;

import com.example.turnpage.domain.follow.dto.FollowRequest.FollowMemberRequest;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowId;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowingFollowerList;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import com.example.turnpage.domain.member.entity.Member;

import java.util.List;

public interface FollowService {
    FollowId followMember(Member member, FollowMemberRequest request);
    List<MemberInfo> getFollowingList(Member member);
    FollowingFollowerList getFollowingFollowerList(Member member);
    FollowId unfollowMember(Member member, Long followerId);
}
