package com.example.turnpage.domain.member.service;


import com.example.turnpage.domain.member.dto.MemberRequest;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.utils.S3FileComponent;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@DisplayName("Member Service의")
@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {
    @Autowired protected MemberService memberService;
    @MockBean protected S3FileComponent s3FileComponent;

    @Test
    @Transactional
    public void 마이페이지_내정보_조회() {

        //when
        MyPageInfo memberInfo =  memberService.getMyPageInfo(member);

        //then
        assertEquals("수밈", memberInfo.getName());
        assertEquals("sumin@gmail.com", memberInfo.getEmail());
        assertEquals(member.getImage(), memberInfo.getProfileImage());
        assertEquals(0, memberInfo.getPoint());
        assertEquals(0, memberInfo.getReportCount());
        assertEquals(0, memberInfo.getSaleCount());
        assertEquals(0, memberInfo.getPurchaseCount());

    }

    @Test
    @Transactional
    public void 마이페이지_내정보_조회_실패_해당멤버없음() {

        //given
        Member newMember =  Member.builder()
                .id(3L)
                .name("새멤버")
                .email("new@gmail.com")
                .inviteCode("aaaaaa")
                .image(null)
                .role(Role.USER)
                .socialType(SocialType.GOOGLE)
                .point(0)
                .reportCount(0)
                .purchaseCount(0)
                .saleCount(0)
                .build();

        //when && then
        assertThrows(BusinessException.class , () -> memberService.getMyPageInfo(newMember));
    }

    @Test
    @Transactional
    public void 마이페이지_내정보_수정() {

        //given
        MemberRequest.EditMyPageRequest request = new MemberRequest.EditMyPageRequest("수밈");
        MultipartFile profileImage = new MockMultipartFile("image", "profileImage.jpg", "image/jpeg", "file content".getBytes());

        given(s3FileComponent.uploadFile(anyString(),any(MultipartFile.class))).willReturn("profile image url");

        //when
        MemberResponse.MemberId memberId =  memberService.editMyPageInfo(member, request, profileImage);
        Member editMember = memberService.findMember(memberId.getMemberId());
        //then
        assertEquals("수밈", editMember.getName());
        assertEquals("profile image url", editMember.getImage());
    }


    @Test
    @Transactional
    public void 포인트_충전() {

        //given
        int point = 500;

        //when
        MemberResponse.MyPoint myPoint = memberService.chargeMyPoint(member, point);

        //then
        assertEquals(member.getId(), myPoint.getMemberId());
        assertEquals(500, myPoint.getTotalPoint());
    }
}
