package com.example.turnpage.global.config.security.attribute;

import java.util.Map;

public class GoogleAttribute extends OAuth2Attribute {
    public static final String GOOGLE_PROVIDER = "google";
    private static final String NAME_ATTRIBUTE_KEY = "sub";

    public GoogleAttribute(Map<String, Object> attributes) {
        super(GOOGLE_PROVIDER, attributes);

        this.email = String.valueOf(attributes.get("email"));
        // 닉네임은 선택적 동의 항목임으로 "null"이 삽입될 수 있음 (null 아니고 "null")
        this.nickname = String.valueOf(attributes.get("name"));
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
