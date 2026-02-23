package com.monorama.iot_server.dto;

import com.monorama.iot_server.exception.ErrorCode;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

@Getter
public class ExceptionDto {

    private final Integer code;
    private final String message;

    @Nullable
    private final List<FieldErrorDto> fieldList;

    public ExceptionDto(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
        this.fieldList = null;
    }

    public ExceptionDto(ErrorCode errorCode, String message, @Nullable List<FieldErrorDto> fieldList) {
        this.code = errorCode.getCode();
        this.message = message;
        this.fieldList = fieldList;
    }

    public record FieldErrorDto(String field, String message) {}
}
