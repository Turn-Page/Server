package com.example.turnpage.global.config.security.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.global.config.security.user.CustomOAuth2User;
import com.example.turnpage.global.config.security.attribute.GoogleAttribute;
import com.example.turnpage.global.config.security.attribute.KakaoAttribute;
import com.example.turnpage.global.config.security.attribute.OAuth2Attribute;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Resource Server에서 사용자 정보를 조회한다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        final OAuth2Attribute oAuth2Attribute;
        if (registrationId.equals(KakaoAttribute.KAKAO_PROVIDER)) {
            oAuth2Attribute = new KakaoAttribute(originAttributes);
        } else if (registrationId.equals(GoogleAttribute.GOOGLE_PROVIDER)) {
            oAuth2Attribute = new GoogleAttribute(originAttributes);
        } else {
            return null;
        }

        Member member = saveOrUpdate(oAuth2Attribute);
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString()));

        return new CustomOAuth2User(authorities, oAuth2Attribute.getAttributes(), oAuth2Attribute.getNameAttributesKey(),
                oAuth2Attribute);
    }

    // 이미 가입된 회원인지 판단 후, 그에 따라 DB 저장 및 변경 수행
    public Member saveOrUpdate(OAuth2Attribute oAuth2Attribute) {
        Member member = memberRepository.findByEmail(oAuth2Attribute.getEmail())
                // member를 update해야 하는가? 소셜 플랫폼의 정보와 우리 서비스의 정보는 별도로 관리할 건데.
                // .map(member -> member.update(oAuth2Attribute.getNickname()))
                .orElse(oAuth2Attribute.toEntity());

        return memberRepository.save(member);
    }
}
