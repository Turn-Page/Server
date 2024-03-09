package com.example.turnpage.global.config.security.util;

import com.example.turnpage.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final SecretKey secretKey;

    // "HMACSHA256" 알고리즘명을 저장
    private static final String SIGNATURE_ALGORITHM = Jwts.SIG.HS256.key().build().getAlgorithm();
    private static final String PAYLOAD_AUTHORITIES_KEY = "authorities";
    private static final String PAYLOAD_MEMBER_ID_KEY = "memberId";
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long expiredMs;

    JwtUtils(@Value("${jwt.secret}") String secret) {
        System.out.println("===키 바이트 길이: " + secret.getBytes(StandardCharsets.UTF_8).length + "===");
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM);
    }

    // 필요한 메소드 목록
    // JWT 발급 메소드
    // JWT 검증 메소드

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());
    }

    public String createJwt(String username, String role) {
        return Jwts.builder()
                .claim("username", username)
                .claim("authorities", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs * 10))
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        final List<SimpleGrantedAuthority> authorities = Arrays.stream(
                payload.get(PAYLOAD_AUTHORITIES_KEY).toString().split(","))
                .map(authority -> "ROLE_" + authority)
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        // 사용자 정의로 구현한 MemberDetails나 Member 엔티티 대신 기존에 제공하는 User 클래스를 사용한다.
        // 추후 우리가 구현한 것으로 대체할 수 있는지 고려 필요함
        final Long memberId = payload.get(PAYLOAD_MEMBER_ID_KEY, Long.class);
        final User principal = new User(String.valueOf(memberId), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
