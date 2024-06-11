package com.example.turnpage.domain.report.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            LocalDate startDate = getFieldValue(obj, startDateField);
            LocalDate endDate = getFieldValue(obj, endDateField);

            if (startDate == null || endDate == null) {
                return true; // null인 경우에는 다른 Valid 어노테이션이 처리하도록 넘긴다.
            }

            return startDate.isBefore(endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private LocalDate getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> currentClass = obj.getClass();
        while (currentClass != null) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                field.setAccessible(true);

                return (LocalDate) field.get(obj);
            } catch (NoSuchFieldException e) {
                // 상위 클래스 필드 탐색을 위해 클래스 이동
                currentClass = currentClass.getSuperclass();
            }
        }

        throw new NoSuchFieldException(String.format("%s 필드를 주어진 클래스에서 찾을 수 없습니다.", fieldName));
    }
}
