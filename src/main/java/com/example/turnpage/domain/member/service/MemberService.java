package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.dto.MemberSignupRequestDto;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;


    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 memberId를 가진 회원이 존재하지 않습니다."));
    }

}
