package com.example.turnpage.domain.member.entity.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken implements Serializable {
    @Id
    @Indexed
    private String refreshToken;

    @Indexed
    private Long memberId;

    @TimeToLive
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long expiration;


    public RefreshToken(final Long memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}