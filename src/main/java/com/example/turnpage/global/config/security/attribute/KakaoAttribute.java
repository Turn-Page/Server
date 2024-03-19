package com.example.turnpage.global.config.security.attribute;

import java.util.Map;

public class KakaoAttribute extends OAuth2Attribute {

    public static final String KAKAO_PROVIDER = "kakao";
    private static final String NAME_ATTRIBUTE_KEY = "id";

    public KakaoAttribute(Map<String, Object> attributes) {
        super(KAKAO_PROVIDER, attributes);

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        this.email = String.valueOf(kakaoAccount.get("email"));
        // 닉네임은 선택적 동의 항목임으로 "null"이 삽입될 수 있음 (null 아니고 "null")
        this.nickname = String.valueOf(profile.get("nickname"));
        this.nameAttributesKey = NAME_ATTRIBUTE_KEY;
    }

    @Override
    public String getProvider() {
        return this.provider;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(this.attributes.get(nameAttributesKey));
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }
}
