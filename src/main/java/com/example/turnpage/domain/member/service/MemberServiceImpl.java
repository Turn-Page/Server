package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    @Override
    public MyPageInfo getMyPageInfo(Member loginMember) {
            Member member = findMember(loginMember.getId());

        return memberConverter.toMyPageInfo(member.getName(), member.getEmail(), member.getImage(),
                member.getInviteCode(),member.getPoint(), member.getReportCount(), member.getSaleCount(), member.getPurchaseCount());
    }


    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 memberId를 가진 회원이 존재하지 않습니다."));
    }

}
