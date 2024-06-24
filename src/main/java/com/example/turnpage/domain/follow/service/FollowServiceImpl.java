package com.example.turnpage.domain.follow.service;

import com.example.turnpage.domain.follow.converter.FollowConverter;
import com.example.turnpage.domain.follow.dto.FollowRequest;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowId;
import com.example.turnpage.domain.follow.dto.FollowResponse.FollowingFollowerList;
import com.example.turnpage.domain.follow.entity.Follow;
import com.example.turnpage.domain.follow.repository.FollowRepository;
import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberInfo;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.turnpage.global.error.code.FollowErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final MemberService memberService;
    private final FollowConverter followConverter;
    private final MemberConverter memberConverter;

    @Transactional
    @Override
    public FollowId followMember(Member member, FollowRequest.FollowMemberRequest request) {
        Member follower = memberService.findMember(request.getEmail());
        validateNotMyself(member, follower);
        validateAlreadyFollow(member, follower);

        Follow follow = followConverter.toEntity(member, follower);

        followRepository.save(follow);
        return followConverter.toFollowId(follow.getId());
    }

    @Override
    public List<MemberInfo> getFollowingList(Member member) {
        List<Member> followingList = followRepository.findByMemberId(member.getId())
                .stream()
                .map(follow -> follow.getFollower())
                .toList();

        return memberConverter.toMemberInfoList(followingList);
    }

    @Override
    public FollowingFollowerList getFollowingFollowerList(Member member) {
        List<Member> followingList = followRepository.findByMemberId(member.getId())
                .stream()
                .map(follow -> follow.getFollower())
                .toList();

        List<Member> followerList = followRepository.findByFollowerId(member.getId())
                .stream()
                .map(follow -> follow.getMember())
                .toList();

        return followConverter.toFollowingFollowerList(followingList, followerList,
                    followingList.size(), followerList.size());
    }

    @Transactional
    @Override
    public FollowId unfollowMember(Member member, Long followerId) {
        Follow follow = followRepository.findByMemberIdAndFollowerId(member.getId(), followerId)
                .orElseThrow(() -> new BusinessException(FOLLOW_NOT_FOUND));

        follow.delete();
        return followConverter.toFollowId(follow.getId());
    }

    private void validateNotMyself(Member member, Member follower) {
        Long memberId = member.getId();
        Long followerId = follower.getId();
        if (memberId.equals(followerId)) {
            throw new BusinessException(CANNOT_FOLLOW_MYSELF);
        }
    }

    private void validateAlreadyFollow(Member member, Member follower) {
        if (followRepository.existsByMemberIdAndFollowerId(member.getId(), follower.getId())) {
            throw new BusinessException(ALREADY_FOLLOWING);
        }
    }
}
