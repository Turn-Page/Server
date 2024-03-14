package com.example.turnpage.domain.member.dto;

import com.example.turnpage.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupRequestDto {
    private String username;
    private String password;

    public Member toEntity(String role) {
        return Member.builder()
                .name(username)
                .email(username)
                .password(password)
                .role(role)
                .inviteCode(RandomStringUtils.random(10, true, true))
                .build();
    }
}
