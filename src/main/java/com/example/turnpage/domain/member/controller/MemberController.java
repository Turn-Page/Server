package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPoint;
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
import static com.example.turnpage.global.result.code.MemberResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버 API", description = "멤버 API입니다.")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지 프로필 조회 API", description = "프로필 조회 API 입니다." )
    @GetMapping("/myPage")
    public ResultResponse<MyPageInfo> getMyPageInfo(@LoginMember Member member) {
        return ResultResponse.of(MYPAGE_INFO, memberService.getMyPageInfo(member));
    }

    @Operation(summary = "마이페이지 프로필 수정 API", description = "프로필 수정 API 입니다." +
            "사용자 닉네임과, 프로필 사진을 변경할 수 있습니다.")
    @PatchMapping(value = "/myPage")
    public ResultResponse<MemberId> editMyPageInfo(@LoginMember Member member,
                                                   @RequestPart(value = "request") EditMyPageRequest request,
                                                   @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        return ResultResponse.of(EDIT_MYPAGE_INFO, memberService.editMyPageInfo(member, request, profileImage));
    }

    @Operation(summary = "포인트 충전 API", description = "포인트 충전 API 입니다." +
            "파라미터로 충전할 포인트를 주세요.")
    @PatchMapping(value = "/myPoint")
    public ResultResponse<MyPoint> chargeMyPoint(@LoginMember Member member,
                                                 @RequestParam(value = "point") int point) {
        return ResultResponse.of(CHARGE_MY_POINT, memberService.chargeMyPoint(member, point));
    }

    @Operation(summary = "포인트 조회 API", description = "내 포인트 조회 API입니다.")
    @GetMapping(value = "/myPoint")
    public ResultResponse<MyPoint> getMyPoint(@LoginMember Member member) {
        return ResultResponse.of(GET_MY_POINT, memberService.getMyPoint(member));
    }
}
