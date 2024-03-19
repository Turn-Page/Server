package com.example.turnpage.global.config.security.user;

import com.example.turnpage.global.config.security.attribute.OAuth2Attribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {
    private OAuth2Attribute oAuth2Attribute;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey,
                            OAuth2Attribute oAuth2Attribute) {
        super(authorities,attributes,nameAttributeKey);
        this.oAuth2Attribute = oAuth2Attribute;
    }

    public OAuth2Attribute getoAuth2Attribute() {
        return oAuth2Attribute;
    }

    public String getEmail() {
        return this.oAuth2Attribute.getEmail();
    }
}
