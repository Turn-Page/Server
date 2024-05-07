package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("Member 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest extends ControllerTestConfig {
    @MockBean private MemberService memberService;

}
