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
    private final PasswordEncoder passwordEncoder;

    public Long signup(MemberSignupRequestDto memberSignupRequestDto) {
        // 이메일 중복 검증 코드
        validateEmailDuplication(memberSignupRequestDto.getUsername());

        String encodedPassword = passwordEncoder.encode(memberSignupRequestDto.getPassword());
        memberSignupRequestDto.setPassword(encodedPassword);

        // admin이 이름인 회원의 경우 권한을 ADMIN으로 설정한다
        final Member member;
        if (memberSignupRequestDto.getUsername().equals("admin")) {
            member = memberSignupRequestDto.toEntity("ADMIN");
        } else {
            member = memberSignupRequestDto.toEntity("USER");
        }

        Long memberId = memberRepository.save(member).getId();
        return memberId;
    }

    private void validateEmailDuplication(String email) {
        boolean isDuplicated = memberRepository.existsByEmail(email);

        if (isDuplicated) {
            throw new RuntimeException(email + "은 이미 회원가입된 이메일입니다.");
        }
    }
}
