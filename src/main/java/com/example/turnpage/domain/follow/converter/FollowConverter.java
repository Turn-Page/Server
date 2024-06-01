package com.example.turnpage.domain.follow.converter;

import com.example.turnpage.domain.follow.dto.FollowResponse.FollowId;
import com.example.turnpage.domain.follow.entity.Follow;
import com.example.turnpage.domain.member.entity.Member;
import org.springframework.stereotype.Component;

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
}
