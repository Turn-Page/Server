package com.example.turnpage.support;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import com.example.turnpage.global.config.security.service.MemberDetails;
import com.example.turnpage.global.config.security.service.MemberDetailsService;
import com.example.turnpage.global.config.security.util.JwtUtils;
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
    JwtUtils jwtUtils;
    @MockBean
    MemberDetailsService memberDetailsService;
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
                .id(1L)
                .role(Role.USER)
                .email("sumin@gmail.com")
                .name("수밈")
                .inviteCode("sefuhdjs")
                .point(0)
                .socialType(SocialType.GOOGLE)
                .reportCount(0)
                .purchaseCount(0)
                .saleCount(0)
                .image(null).build();
    }


}
