package com.example.turnpage.domain.follow.repository;

import com.example.turnpage.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByMemberId(Long memberId);
    List<Follow> findByFollowerId(Long followerId);

    Optional<Follow> findByMemberIdAndFollowerId(Long memberId, Long followerId);
}
