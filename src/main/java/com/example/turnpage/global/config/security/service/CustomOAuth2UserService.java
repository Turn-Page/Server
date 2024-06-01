package com.example.turnpage.global.config.security.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.global.config.security.attribute.OAuthAttribute;
import com.example.turnpage.global.config.security.user.CustomOAuth2User;
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
import java.util.Optional;
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

        final OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, originAttributes);
        if (oAuthAttribute == null) {
            return null;
        }

        Member member = saveOrUpdate(oAuthAttribute);
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString()));

        return new CustomOAuth2User(authorities, oAuthAttribute.getAttributes(), oAuthAttribute.getUsernameAttributeKey(),
                oAuthAttribute);
    }

    // 이미 가입된 회원인지 판단 후, 결과에 따라 데이터 변경 및 저장 수행
    public Member saveOrUpdate(OAuthAttribute oAuthAttribute) {
        Member member = memberRepository.findByEmail(oAuthAttribute.getEmail())
                .map(entity -> entity.update(oAuthAttribute.getName(), oAuthAttribute.getProfileImage()))
                .orElse(oAuthAttribute.toEntity());

        return memberRepository.save(member);
    }
}
