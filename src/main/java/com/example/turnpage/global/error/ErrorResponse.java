package com.example.turnpage.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;


    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

        public static ValidationError of(final ObjectError objectError) {
            return ValidationError.builder()
                    .field(objectError.getObjectName())
                    .message(objectError.getDefaultMessage())
                    .build();
        }
    }
}
