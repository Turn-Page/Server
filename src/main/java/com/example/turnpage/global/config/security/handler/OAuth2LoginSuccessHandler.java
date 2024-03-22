package com.example.turnpage.global.config.security.handler;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.domain.member.service.redis.RefreshTokenService;
import com.example.turnpage.global.config.security.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.turnpage.global.config.security.user.CustomOAuth2User;
import com.example.turnpage.global.config.security.util.CookieUtils;
import com.example.turnpage.global.config.security.util.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository
            oAuth2AuthorizationRequestBasedOnCookieRepository;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 email을 가진 회원이 존재하지 않습니다."));

        String role = oAuth2User.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        // access token
        String accessToken = jwtUtils.createJwt(email, role, ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        CookieUtils.addCookie(response, AUTHORIZATION_HEADER, accessToken, ACCESS_TOKEN_VALIDITY_IN_SECONDS.intValue());

        // refresh token
        String refreshToken = jwtUtils.createJwt(email, role, REFRESH_TOKEN_VALIDITY_IN_SECONDS);
        refreshTokenService.saveRefreshToken(member.getId(), refreshToken);
        CookieUtils.addCookie(response, REFRESH_TOKEN_KEY, refreshToken, REFRESH_TOKEN_VALIDITY_IN_SECONDS.intValue());

        clearAuthenticationAttributes(request, response);
        response.sendRedirect("http://localhost.com:8080/public");
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        oAuth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequest(request, response);
    }
}
