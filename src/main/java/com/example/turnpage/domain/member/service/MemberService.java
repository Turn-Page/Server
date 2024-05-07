package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.entity.Member;

public interface MemberService {
    Member findMember(Long memberId);
}
