package com.example.turnpage.domain.member.service;

import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.dto.MemberRequest.EditMyPageRequest;
import com.example.turnpage.domain.member.dto.MemberResponse;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.dto.MemberResponse.MyPageInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.repository.MemberRepository;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.MemberErrorCode;
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
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public MemberId editMyPageInfo(Member loginMember, EditMyPageRequest request, MultipartFile profileImage) {
        Member member = findMember(loginMember.getId());

        member.update(request.getName(), updateProfileImage(member, profileImage));

        return new MemberId(member.getId());
    }

    @Override
    @Transactional
    public MemberResponse.MyPoint chargeMyPoint(Member loginMember, int point) {
        Member member = findMember(loginMember.getId());

        int totalPoint = member.chargePoint(point);

        return memberConverter.toMyPoint(member.getId(), totalPoint);
    }

    private String updateProfileImage(Member member, MultipartFile profileImage) {
        //프로필 이미지가 수정되어 파라피터로 들어왔을 때
        if (profileImage != null) {
            //s3에서 이전 프로필 이미지 삭제
            if (member.getImage() != null && !checkSocialProfileImage(member.getImage()))
                s3FileComponent.deleteFile(member.getImage());
            //s3에 수정된 이미지 업로드
            return s3FileComponent.uploadFile("member", profileImage);
        }
        return member.getImage();
    }

    //소셜로그인에서 받아온 프로필 이미지인지 확인
    private boolean checkSocialProfileImage(String imageUrl) {
        return imageUrl.startsWith("https://lh3.googleusercontent.com") ||
                        imageUrl.startsWith("https://t1.kakaocdn.net");
    }
}
