package com.example.turnpage.domain.member.service.redis;

import com.example.turnpage.domain.member.entity.redis.RefreshToken;
import com.example.turnpage.domain.member.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("해당 refreshToken이 존재하지 않습니다."));
    }

    public void saveRefreshToken(Long memberId, String refreshToken) {
        RefreshToken updatedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(originalRefreshToken -> originalRefreshToken.update(refreshToken))
                .orElse(new RefreshToken(memberId, refreshToken));

        refreshTokenRepository.save(updatedRefreshToken);
    }
}
