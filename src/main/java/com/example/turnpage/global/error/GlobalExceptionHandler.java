package com.example.turnpage.global.error;

import com.example.turnpage.global.error.domain.GlobalErrorCode;
import com.example.turnpage.global.error.exception.BusinessException;
import com.example.turnpage.global.error.exception.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;

import static com.example.turnpage.global.error.domain.GlobalErrorCode.INVALID_INPUT;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // required=true인 @RequestParam에 값이 매핑되지 않을 경우 발생하는 예외 처리
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT.getErrorCode(), e.getParameterName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @PathVariable이나 @RequestParam에 매핑된 값이 @Valid 어노테이션의 조건을 지키지 않을 경우 발생하는 예외 처리
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT.getErrorCode(), e.getConstraintViolations());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @RequsetBody가 붙은 Dto 클래스의 필드에 매핑된 값이 @Valid 어노테이션의 조건을 지키지 않을 경우 발생하는 예외 처리
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.of(INVALID_INPUT.getErrorCode(), e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // @ModelAttribut가 붙은 클래스의 필드에 매핑된 값이 @Valid 어노테이션의 조건을 지키지 않을 경우 발생하는 예외 처리
    // 해당 예외 처리 함수의 로직은 'handleMethodArgumentNotValidException(MethodArgumentNotValidException e)'와 동일하다.
    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT.getErrorCode(), e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // multipart/form-data 요청에 특정 문제가 생길 시 발생하는 예외 처리
    // ex) 파일 데이터 일부 손실
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(
            MissingServletRequestPartException e) {
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT.getErrorCode(), e.getRequestPartName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 요청에 포함되어 있어야 하는 쿠키가 없어 매핑되지 못할 경우 발생하는 예외 처리
    @ExceptionHandler(value = MissingRequestCookieException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(
            MissingRequestCookieException e) {
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT.getErrorCode(), e.getCookieName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // enum 타입으로 값을 매핑할 때, 대응하는 enum 상수가 없을 경우 발생하는 예외 처리
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // JSON 파싱 시 JSON 형식을 지키지 않았거나, Request Body와 구조가 다른 이유 등으로 Converter가 데이터를 읽지 못할 경우 발생하는 예외 처리
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.HTTP_MESSAGE_NOT_READABLE.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 현재 요청의 HTTP METHOD를 지원하지 않을 경우 발생하는 예외 처리
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        final List<ErrorResponse.FieldError> errors = new ArrayList<>();
        errors.add(new ErrorResponse.FieldError("http method", e.getMethod(), GlobalErrorCode.METHOD_NOT_ALLOWED.getMessage()));
        final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.HTTP_HEADER_INVALID.getErrorCode(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // DB의 테이블을 조회해 반환받은 엔티티 클래스가 없을 경우 발생하는 예외 처리
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 비즈니스 로직 단에서 발생하는 예외 처리
    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    // 위 예외들에 해당하지 않는 나머지 예외 처리
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
