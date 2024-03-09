package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.dto.MemberLoginRequestDto;
import com.example.turnpage.domain.member.dto.MemberSignupRequestDto;
import com.example.turnpage.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final MemberService memberService;

    @GetMapping("/auth/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/auth/signup")
    @ResponseBody
    public String signup(@RequestParam String username, String password) {
        MemberSignupRequestDto memberSignupRequestDto = new MemberSignupRequestDto();
        memberSignupRequestDto.setUsername(username);
        memberSignupRequestDto.setPassword(password);

        Long memberId = memberService.signup(memberSignupRequestDto);
        return "회원가입에 성공하였습니다. 새로 등록된 회원의 memberId: " + memberId;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @PostMapping("/auth/login")
    public @ResponseBody String loginProcess(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return "hi";
    }
}
