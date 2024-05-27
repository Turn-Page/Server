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
            Field startField = obj.getClass().getDeclaredField(startDateField);
            Field endField = obj.getClass().getDeclaredField(endDateField);
            startField.setAccessible(true);
            endField.setAccessible(true);

            LocalDate startDate = (LocalDate) startField.get(obj);
            LocalDate endDate = (LocalDate) endField.get(obj);

            if (startDate == null || endDate == null) {
                return true; // null인 경우에는 다른 Valid 어노테이션이 처리하도록 넘긴다.
            }

            return startDate.isBefore(endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
