package com.example.turnpage.support;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import com.example.turnpage.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class ServiceTestConfig {
    @Autowired
    protected MemberRepository memberRepository;
    protected Member member;

    @BeforeEach
    public void setUp() {
        member = createMember();
    }

    protected Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .name("수밈")
                        .email("sumin@gmail.com")
                        .inviteCode("aaaaaa")
                        .image("http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
                        .role(Role.USER)
                        .socialType(SocialType.GOOGLE)
                        .point(0)
                        .reportCount(0)
                        .purchaseCount(0)
                        .saleCount(0)
                        .build()
        );
    }
}
