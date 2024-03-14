package com.example.turnpage.global.config.security.filter;

import com.example.turnpage.domain.member.dto.MemberLoginRequestDto;
import com.example.turnpage.global.config.security.member.MemberDetails;
import com.example.turnpage.global.config.security.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
            "/auth/login", "POST");
    private static final String JWT_PREFIX = "Bearer ";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        setRequiresAuthenticationRequestMatcher(ANT_PATH_REQUEST_MATCHER);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            // Request에서 회원의 로그인 정보 추출
            String requestBody = IOUtils.toString(request.getReader());
            MemberLoginRequestDto memberLoginRequestDto = objectMapper.readValue(requestBody, MemberLoginRequestDto.class);

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                    .unauthenticated(memberLoginRequestDto.getUsername(), memberLoginRequestDto.getPassword());

            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException("IOUtils를 활용한 request body 객체로 변환 중 문제가 발생하였습니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // JWT 발급
        MemberDetails memberDetails = (MemberDetails) authResult.getPrincipal();
        String username = memberDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = memberDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        String jwt = jwtUtils.createJwt(username, role);

        response.addHeader("Authorization", JWT_PREFIX + jwt);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
