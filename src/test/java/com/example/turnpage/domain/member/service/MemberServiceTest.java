package com.example.turnpage.domain.member.service;



import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.entity.Role;
import com.example.turnpage.domain.member.entity.SocialType;
import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Member Service의")
@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {
    @Autowired MemberService memberService;

    @Test
    @Transactional
    public void 마이페이지_내정보_조회() {

        //when
        MyPageInfo memberInfo =  memberService.getMyPageInfo(member);

        //then
        assertEquals("수밈", memberInfo.getName());
        assertEquals("sumin@gmail.com", memberInfo.getEmail());
        assertEquals(member.getImage(), memberInfo.getImage());
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
}
