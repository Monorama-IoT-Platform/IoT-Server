package com.monorama.iot_server.dto.response.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AQDResponseDto(
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        Double pm25Value,
        Integer pm25Level,
        Double pm10Value,
        Integer pm10Level,
        Double temperature,
        Integer temperatureLevel,
        Double humidity,
        Integer humidityLevel,
        Double co2Value,
        Integer co2Level,
        Double vocValue,
        Integer vocLevel,
        Double picoDeviceLatitude,
        Double picoDeviceLongitude
) {}