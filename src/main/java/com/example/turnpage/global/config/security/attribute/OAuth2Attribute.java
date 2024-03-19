package com.example.turnpage.global.config.security.attribute;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2Attribute {
    protected final String provider;

    protected final Map<String, Object> attributes;

    protected String email;
    protected String nickname;
    // 이건 머지? 수민이한테 물어봐야 함
    protected String nameAttributesKey;

    public OAuth2Attribute(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }


    // 제공자(Resource Server)
    public abstract String getProvider();

    // 제공자에서 발급해주는 아이디(번호)
    public abstract String getProviderId();

    // 이메일
    public abstract String getEmail();

    // 사용자 실명 (설정한 이름)
    public abstract String getNickname();

    public Member toEntity() {
        return Member.builder()
                .name(nickname)
                .email(email)
                // 임시로 OAuth 가입 시 USER로 권한 세팅함. 추후 고민
                .role(Role.USER.toString())
                .image(null)
                .socialType(provider.toUpperCase())
                .build();
    }
}
