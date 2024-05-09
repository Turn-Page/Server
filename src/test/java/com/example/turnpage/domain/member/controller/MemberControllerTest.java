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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
