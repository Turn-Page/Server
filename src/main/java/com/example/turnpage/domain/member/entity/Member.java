package com.example.turnpage.domain.member.entity;

import com.example.turnpage.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is NULL")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(name = "invite_code", nullable = false)
    private String inviteCode;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int point;

    @ColumnDefault("0")
    private int reportCount;

    @ColumnDefault("0")
    private int saleCount;

    @ColumnDefault("0")
    private int purchaseCount;


    @Builder
    public Member(String name, String email, String role, String image, String socialType) {
        this.name = name;
        this.email = email;
        this.role = Role.valueOf(role);
        this.image = image;
        this.socialType = SocialType.valueOf(socialType);
        this.inviteCode = RandomStringUtils.random(10, true, true);
        this.point = 0;
    }

    public Member update(String name, String image) {
        this.name = name;
        this.image = image;

        return this;
    }

    public int addPoint(int point) {
        this.point += point;
        return this.point;
    }

    public int subtractPoint(int point) {
        this.point -= point;
        return this.point;
    }

    public void incrementSaleCount() {
        this.saleCount += 1;
    }
    public void incrementPurchaseCount() {
        this.purchaseCount += 1;
    }

    public void incrementReportCount() {
        this.reportCount += 1;
    }

    public void decrementReportCount() {
        this.reportCount -= 1;
    }
}
