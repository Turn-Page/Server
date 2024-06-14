package com.example.turnpage.domain.follow.service;


import com.example.turnpage.domain.follow.dto.FollowRequest;
import com.example.turnpage.domain.follow.dto.FollowResponse;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowingFollowerList;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Follow Service 의")
@SpringBootTest
public class FollowServiceTest extends ServiceTestConfig {
    @Autowired FollowService followService;

    @Test
    @Transactional
    @DisplayName("팔로잉, 팔로워 목록 조회 성공")
    public void getFollowingFollowerList() {

        //when
        followService.followMember(testMember,new FollowRequest.FollowMemberRequest(testMember2.getEmail()));
        followService.followMember(testMember3,new FollowRequest.FollowMemberRequest(testMember.getEmail()));

        FollowingFollowerList followingFollowerList = followService.getFollowingFollowerList(testMember);
        //then
        assertEquals(1, followingFollowerList.getFollowingCount());
        assertEquals(1, followingFollowerList.getFollowerCount());
        assertEquals("강연", followingFollowerList.getFollowingInfoList().get(0).getMemberInfo().getName());
        assertEquals("태혁님", followingFollowerList.getFollowerInfoList().get(0).getMemberInfo().getName());
    }
}
