package com.example.turnpage.domain.member.service;



import com.example.turnpage.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
