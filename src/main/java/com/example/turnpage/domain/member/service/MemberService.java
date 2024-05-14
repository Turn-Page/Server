package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.dto.MemberRequest;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import static com.example.turnpage.domain.member.dto.MemberRequest.*;
import static com.example.turnpage.domain.member.dto.MemberResponse.*;

public interface MemberService {
    Member findMember(Long memberId);
    MyPageInfo getMyPageInfo(Member loginMember);
    MemberId editMyPageInfo(Member loginMember, EditMyPageRequest request, MultipartFile profileImage);
}
