package com.example.turnpage.support;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import com.example.turnpage.global.config.security.service.MemberDetails;
import com.example.turnpage.global.config.security.service.MemberDetailsService;
import com.example.turnpage.global.config.security.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestConfig {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JwtUtils jwtUtils;
    @MockBean
    protected MemberDetailsService memberDetailsService;
    protected String jwt;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    protected Member member;

    @BeforeEach
    public void setUp() {
        member = createMember();
        jwt = jwtUtils.createJwt(member.getEmail(), String.valueOf(member.getRole()), ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        when(memberDetailsService.loadUserByUsername(member.getEmail())).thenReturn(new MemberDetails(member));
    }

    protected Member createMember() {
        return Member.builder()
                .id(100L)
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
                .build();
    }


}
