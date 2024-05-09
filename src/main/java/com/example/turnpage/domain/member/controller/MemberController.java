package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.global.result.code.MemberResultCode.MYPAGE_INFO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "멤버 API입니다.")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지 프로필 조회 API", description = " 프로필 조회 API 입니다." )
    @GetMapping("/mypage")
    public ResultResponse<MyPageInfo> getMyPageInfo(@LoginMember Member member) {
        return ResultResponse.of(MYPAGE_INFO.getResultCode(), memberService.getMyPageInfo(member));
    }
}
