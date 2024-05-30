package com.example.turnpage.domain.friend.repository;

import com.example.turnpage.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findBySenderId(Long memberId);

    /**
     * 특정 memberId를 가진 회원의 친구 관계를 조회하는 메소드
     * 파라미터는 2개이지만, 목적 상 동일한 값이 들어가야 한다.
     */
    List<Friend> findBySenderIdOrReceiverId(Long memberId1, Long memberId2);
}
