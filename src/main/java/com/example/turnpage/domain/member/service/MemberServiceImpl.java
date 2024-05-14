package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.dto.MemberRequest.EditMyPageRequest;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final S3FileComponent s3FileComponent;

    @Override
    public MyPageInfo getMyPageInfo(Member loginMember) {
        Member member = findMember(loginMember.getId());

        return memberConverter.toMyPageInfo(member.getName(), member.getEmail(), member.getImage(),
                member.getInviteCode(), member.getPoint(), member.getReportCount(), member.getSaleCount(), member.getPurchaseCount());
    }


    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 memberId를 가진 회원이 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public MemberId editMyPageInfo(Member loginMember, EditMyPageRequest request, MultipartFile profileImage) {
        Member member = findMember(loginMember.getId());

        member.update(request.getName(), uploadProfileImage(member, profileImage));

        return new MemberId(member.getId());
    }

    @Override
    @Transactional
    public MemberResponse.MyPoint chargeMyPoint(Member loginMember, int point) {
        Member member = findMember(loginMember.getId());

        int totalPoint = member.chargePoint(point);

        return memberConverter.toMyPoint(member.getId(), totalPoint);
    }

    private String uploadProfileImage(Member member, MultipartFile profileImage) {
        if (profileImage != null) {
            if (member.getImage() != null)
                s3FileComponent.deleteFile(member.getImage());
            return s3FileComponent.uploadFile("member", profileImage);
        }
        return member.getImage();
    }


}
