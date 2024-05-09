package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;

public interface MemberService {
    Member findMember(Long memberId);
    MyPageInfo getMyPageInfo(Member loginMember);
}
