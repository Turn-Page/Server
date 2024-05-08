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
    @DisplayName("내 정보 조회 성공 테스트")
    @Transactional
    public void getMyProfileInfoTest() {

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
    @DisplayName("내 정보 조회 실패 테스트 - DB에서 해당 멤버를 찾을 수 없음")
    @Transactional
    public void getMyProfileInfoFailTest() {

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
