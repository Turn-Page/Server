package com.example.turnpage.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("id"),
    GOOGLE("sub");

    private final String usernameAttributeKey;
}
