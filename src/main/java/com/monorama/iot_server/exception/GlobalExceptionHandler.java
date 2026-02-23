package com.monorama.iot_server.exception;

import com.monorama.iot_server.dto.ExceptionDto;
import com.monorama.iot_server.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseDto<?> fail(CommonException e) {
        return ResponseDto.fail(e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseDto<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.ACCESS_DENIED_ERROR));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseDto<?> handleAuthenticationException(AuthenticationException e) {
        log.error("AuthenticationException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.INVALID_TOKEN_ERROR));
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseDto<?> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        log.error("OAuth2AuthenticationException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.SOCIAL_LOGIN_ERROR));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseDto<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("HttpMediaTypeNotSupportedException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseDto<?> handleMultipartException(MultipartException e) {
        log.error("MultipartException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.FILE_UPLOAD_ERROR));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseDto<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.NOT_END_POINT));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseDto<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.INVALID_ARGUMENT));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ExceptionDto.FieldErrorDto> fieldList =
                e.getBindingResult().getFieldErrors().stream()
                        .map(fe -> new ExceptionDto.FieldErrorDto(fe.getField(), fe.getDefaultMessage()))
                        .toList();

        // message는 요약(프론트에서 fieldList 우선 사용)
        String message = fieldList.isEmpty() ? "Invalid Argument" : "Validation failed";

        log.error("MethodArgumentNotValidException: {}, fields={}", e.getMessage(), fieldList.size());
        return ResponseDto.fail(ErrorCode.INVALID_ARGUMENT, message, fieldList);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseDto<?> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        // HandlerMethodValidationException은 구조가 복잡해서,
        // 최소 구현: 메시지는 유지, fieldList는 비워두거나(운영상 충분) 필요하면 확장 파싱 가능
        log.error("HandlerMethodValidationException: {}", e.getMessage());
        return ResponseDto.fail(ErrorCode.INVALID_ARGUMENT, "Validation failed", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDto<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseDto<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseDto<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException: {}", e.getMessage());
        return fail(new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseDto<?> handleApiException(CommonException e) {
        log.error("CommonException: {}", e.getMessage());
        return ResponseDto.fail(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<?> handleException(Exception e) {
        log.error("Unhandled Exception", e);
        return fail(new CommonException(ErrorCode.SERVER_ERROR));
    }
}
