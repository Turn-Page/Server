package com.example.turnpage.domain.member.service;


import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Member Service의")
@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {
    @Autowired MemberService memberService;

}
