package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.turnpage.domain.member.dto.MemberRequest.EditMyPageRequest;
import static com.example.turnpage.global.result.code.MemberResultCode.EDIT_MYPAGE_INFO;
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

    @Operation(summary = "마이페이지 프로필 수정 API", description = " 프로필 수정 API 입니다." +
            "사용자 닉네임과, 프로필 사진을 변경할 수 있습니다.")
    @PatchMapping(value = "/mypage")
    public ResultResponse<MemberId> editMyPageInfo(@LoginMember Member member,
                                                   @RequestPart(value = "request") EditMyPageRequest request,
                                                   @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        return ResultResponse.of(EDIT_MYPAGE_INFO.getResultCode(), memberService.editMyPageInfo(member, request, profileImage));
    }
}
