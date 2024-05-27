package com.example.turnpage.domain.salePost.entity;

import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.GlobalErrorCode;
import com.example.turnpage.global.error.domain.SalePostErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    MINT("최상"),
    TOP("상"),
    MIDDLE("중"),
    BOTTOM("하");

    private final String toKorean;


    public static Grade toGrade(String korean) {
        for (Grade grade : Grade.values()) {
            if (grade.toKorean.equals(korean))
                return grade;
        }

        throw new BusinessException(SalePostErrorCode.INVALID_GRADE_INPUT);
    }


}
