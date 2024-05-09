package com.example.turnpage.domain.member.controller;

import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Member 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest extends ControllerTestConfig {
    @MockBean private MemberService memberService;

    @Test
    @DisplayName("내 정보 조회 성공 테스트")
    public void getMyPageInfoTest() throws Exception {

        //given
        final String url = "/members/mypage";
        MyPageInfo response = MyPageInfo.builder()
                .name(member.getName())
                .email(member.getEmail())
                .inviteCode(member.getInviteCode())
                .profileImage(member.getImage())
                .point(member.getPoint())
                .reportCount(member.getReportCount())
                .saleCount(member.getSaleCount())
                .purchaseCount(member.getPurchaseCount())
                .build();

        given(memberService.getMyPageInfo(any(Member.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get(url).accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SM001"))
                .andExpect(jsonPath("$.data.name").value("수밈"))
                ;

        verify(memberService).getMyPageInfo(member);
    }

    @Test
    @DisplayName("내 정보 수정 성공 테스트")
    public void editMyPageInfoTest() throws Exception {

        //given
        final String url = "/members/mypage";

        EditMyPageInfo request = EditMyPageInfo.builder()
                .profileImage("update.png")
                .name("수정된 닉네임")
                .build();

        EditMyPageInfo response = EditMemberInfo.builder()
                .memberId(1L)
                .profileImage("update.png")
                .name("수정된 닉네임")
                .build();

        given(memberService.editMyPageInfo(any(Member.class), any(EditMemberInfo.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SM002"))
                .andExpect(jsonPath("$.data.id").value("1L"))
        ;

        verify(memberService).updateMyPageInfo(member);
    }

    @Test
    @DisplayName("포인트 충전 성공 테스트")
    public void chargeMyPointTest() throws Exception {

        //given
        final String url = "/members/point";
        int point = 500;

        ChargeMyPoint response = ChargeMyPoint.builder()
                .memberId(1L)
                .totalPoint(500) //충전 후 현재 나의 총 포인트
                .build();

        given(memberService.chargeMyPoint(any(Member.class), eq(point))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post(url).param("point", String.valueOf(point))
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SM002"))
                .andExpect(jsonPath("$.data.id").value("1L"))
        ;

        verify(memberService).chargeMyPoint(member);
    }
}
