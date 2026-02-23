package com.monorama.iot_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monorama.iot_server.exception.CommonException;
import com.monorama.iot_server.exception.ErrorCode;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

public record ResponseDto<T>(
        @JsonIgnore HttpStatus httpStatus,
        @NotNull Boolean success,
        @Nullable T data,
        @Nullable ExceptionDto error
) {
    public static <T> ResponseDto<T> ok(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.OK, true, data, null);
    }

    public static <T> ResponseDto<T> created(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.CREATED, true, data, null);
    }

    public static ResponseDto<Object> fail(final CommonException e) {
        return new ResponseDto<>(
                e.getErrorCode().getHttpStatus(),
                false,
                null,
                new ExceptionDto(e.getErrorCode(), e.getMessage())
        );
    }

    // ✅ fieldList 포함용 오버로드
    public static ResponseDto<Object> fail(final ErrorCode errorCode, final String message,
                                           @Nullable final List<ExceptionDto.FieldErrorDto> fieldList) {
        return new ResponseDto<>(
                errorCode.getHttpStatus(),
                false,
                null,
                new ExceptionDto(errorCode, message, fieldList)
        );
    }

    public static ResponseDto<Object> fail(final HandlerMethodValidationException e) {
        return new ResponseDto<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                new ExceptionDto(ErrorCode.INVALID_ARGUMENT, e.getMessage())
        );
    }

    public static ResponseDto<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseDto<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                new ExceptionDto(ErrorCode.MISSING_REQUEST_PARAMETER, e.getMessage())
        );
    }

    // ✅ MethodArgumentNotValidException에서는 fieldList를 GlobalExceptionHandler에서 파싱해서 위 fail(ErrorCode, message, fieldList) 사용 권장
    public static ResponseDto<Object> fail(final MethodArgumentNotValidException e) {
        return new ResponseDto<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                new ExceptionDto(ErrorCode.INVALID_ARGUMENT, e.getMessage())
        );
    }
}
