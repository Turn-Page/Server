package com.example.turnpage.global.config.security.attribute;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttribute {
    private final String email;
    private final String name;
    private final String profileImage;
    private final SocialType provider;
    private final String usernameAttributeKey;
    private final Map<String, Object> attributes;

    public static OAuthAttribute of(String provider, Map<String, Object> attributes) {
        if (isEqualProvider(provider, SocialType.KAKAO)) {
            return ofKakao(attributes);
        } else if (isEqualProvider(provider, SocialType.GOOGLE)) {
            return ofGoogle(attributes);
        }

        return null;
    }

    private static OAuthAttribute ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttribute.builder()
                .email(String.valueOf(kakaoAccount.get("email")))
                .name(String.valueOf(profile.get("nickname")))
                .profileImage(String.valueOf(profile.get("profile_image_url")))
                .provider(SocialType.KAKAO)
                .usernameAttributeKey(SocialType.KAKAO.getUsernameAttributeKey())
                .attributes(attributes)
                .build();
    }

    private static OAuthAttribute ofGoogle(Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .email(String.valueOf(attributes.get("email")))
                .name(String.valueOf(attributes.get("name")))
                .profileImage(String.valueOf(attributes.get("picture")))
                .provider(SocialType.GOOGLE)
                .usernameAttributeKey(SocialType.GOOGLE.getUsernameAttributeKey())
                .attributes(attributes)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                // 임시로 OAuth 가입 시 USER로 권한 세팅함. 추후 고민
                .role(Role.USER.toString())
                .image(null)
                .socialType(provider.toString())
                .build();
    }

    private static boolean isEqualProvider(String provider, SocialType socialType) {
        return provider.toUpperCase().equals(socialType.toString());
    }
}
