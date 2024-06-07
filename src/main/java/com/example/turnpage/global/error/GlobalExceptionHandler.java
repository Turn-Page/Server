package com.example.turnpage.global.error;

import com.example.turnpage.global.error.ErrorResponse.ValidationError;
import com.example.turnpage.global.error.code.GlobalErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        ErrorCode errorCode = GlobalErrorCode.INVALID_INPUT;
        return handleExceptionInternal(e, errorCode);
    }

    // 비즈니스 로직 단에서 발생하는 예외 처리
    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<Object> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    // 위 예외들에 해당하지 않는 나머지 예외 처리
    @ExceptionHandler
    protected ResponseEntity<Object> handleException(Exception e) {
        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(makeErrorResponse(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(makeErrorResponse(e, errorCode));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());
        GlobalErrorCode errorCode = GlobalErrorCode.convertToGlobalErrorCode(httpStatus);

        return ResponseEntity
                .status(httpStatus)
                .headers(headers)
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        final List<ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ValidationError.of(fieldError))
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}
