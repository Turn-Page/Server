package com.example.turnpage.domain.friend.service;

import com.example.turnpage.domain.friend.repository.FriendRepository;
import com.example.turnpage.domain.member.converter.MemberConverter;
import com.example.turnpage.domain.member.dto.MemberResponse.MemberId;
import com.example.turnpage.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final MemberConverter memberConverter;

    @Override
    public List<MemberId> getFriendList(Member member) {
        List<Member> friendList = friendRepository
                .findBySenderIdOrReceiverId(member.getId(), member.getId())
                .stream()
                .map(friend -> friend.getSender() != member ? friend.getSender() : friend.getReceiver())
                .collect(Collectors.toList());

        return memberConverter.toMemberIdList(friendList);
    }
}
