package com.example.turnpage.domain.member.converter;

import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.LoginInfo;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPoint;
import com.example.turnpage.domain.member.dto.MemberResponse.WriterInfo;
import com.example.turnpage.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public MyPoint toMyPoint(Long memberId, int totalPoint) {
        return MyPoint.builder()
                .memberId(memberId)
                .totalPoint(totalPoint)
                .build();
    }

    public LoginInfo toLoginInfo(Long memberId, String accessToken, String refreshToken) {
        return LoginInfo.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public WriterInfo toWriterInfo(Member member) {
        return WriterInfo.builder()
                .writer(member.getName())
                .profileImage(member.getImage())
                .build();
    }

    public List<MemberId> toMemberIdList(List<Member> memberList) {
        return memberList
                .stream()
                .map(member -> toMemberId(member))
                .collect(Collectors.toList());
    }

    public MemberId toMemberId(Member member) {
        return new MemberId(member.getId());
    }
}
