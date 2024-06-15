package com.example.turnpage.domain.follow.converter;

import com.example.turnpage.domain.follow.dto.FollowResponse.FollowId;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowInfo;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowingFollowerList;
import com.example.turnpage.domain.follow.entity.Follow;
import com.example.turnpage.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;

@Component
public class FollowConverter {
    public Follow toEntity(Member member, Member follower) {
        return Follow.builder()
                .member(member)
                .follower(follower)
                .build();
    }

    public FollowId toFollowId(Long followId) {
        return new FollowId(followId);
    }

    public FollowInfo toFollowInfo(Member member) {
        return FollowInfo.builder()
                .followId(member.getId())
                .memberInfo(new MemberInfo(member.getId(),member.getName(), member.getImage()))
                .build();
    }

    public FollowingFollowerList toFollowingFollowerList(List<Member> followingList, List<Member> followerList,
                                                                        int followingCount, int followerCount) {
        List<FollowInfo> following = followingList.stream().map(this::toFollowInfo).toList();
        List<FollowInfo> follower = followerList.stream().map(this::toFollowInfo).toList();

        return FollowingFollowerList.builder()
                .followingInfoList(following)
                .followerInfoList(follower)
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }
}
