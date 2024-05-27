package com.example.turnpage.domain.friend.service;

import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.entity.Member;

import java.util.List;

public interface FriendService {
    List<MemberId> getFriendList(Member member);
}
