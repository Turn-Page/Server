package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.redis.RefreshToken;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.domain.member.service.redis.RefreshTokenService;
import com.example.turnpage.global.config.security.util.CookieUtils;
import com.example.turnpage.global.config.security.util.JwtUtils;
import com.example.turnpage.global.result.ResultResponse;
import com.example.turnpage.global.result.code.MemberResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

@RequiredArgsConstructor
@Controller
public class AuthController {
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @GetMapping("/auth/reissue")
    @ResponseBody
    public String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenStr = WebUtils.getCookie(request, "refresh_token").getValue();
        RefreshToken refreshToken = refreshTokenService.findByRefreshToken(refreshTokenStr);
        Member member = memberService.findMember(refreshToken.getMemberId());

        String accessToken = jwtUtils.createJwt(member.getEmail(), member.getRole().toString(), ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        response.addHeader(AUTHORIZATION_HEADER, accessToken);
        CookieUtils.addCookie(response, AUTHORIZATION_HEADER, accessToken, ACCESS_TOKEN_VALIDITY_IN_SECONDS.intValue());
        return "reissue";
    }

    // 기존 콜백: http://localhost:8080/callback/kakao
    @GetMapping("/callback/kakao")
    @ResponseBody
    public ResultResponse<String> testLogin() {
        return ResultResponse.of(MemberResultCode.LOGIN.getResultCode());
    }
}
