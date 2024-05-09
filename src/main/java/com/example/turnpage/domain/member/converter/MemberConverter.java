package com.example.turnpage.domain.member.converter;

import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public MyPageInfo toMyPageInfo(String name, String email, String profileImage,
                                   String inviteCode, int point, int reportCount, int saleCount ,int purchaseCount) {
        return MyPageInfo.builder()
                .name(name)
                .email(email)
                .profileImage(profileImage)
                .inviteCode(inviteCode)
                .point(point)
                .reportCount(reportCount)
                .saleCount(saleCount)
                .purchaseCount(purchaseCount)
                .build();
    }
}
