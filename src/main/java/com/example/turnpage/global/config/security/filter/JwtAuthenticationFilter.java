package com.example.turnpage.global.config.security.filter;

import com.example.turnpage.global.config.security.util.JwtUtils;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.code.AuthErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_TYPE = "Bearer ";
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        //token이 없으면 anonymous User
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        validateJwtAuthorizationType(authorization);
        String jwt = authorization.substring(AUTHORIZATION_TYPE.length());

        //token 검증이 완료된 경우에만 authentication을 부여
        if (jwtUtils.validateToken(jwt)) {
            System.out.println("jwt: " + jwt);
            Authentication authentication = jwtUtils.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
    private void validateJwtAuthorizationType(String authorization) {
        if (!authorization.startsWith(AUTHORIZATION_TYPE))
            throw new BusinessException(AuthErrorCode.UNSUPPORTED_JWT);
    }

}
