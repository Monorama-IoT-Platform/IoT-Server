package com.monorama.iot_server.dto.response.airquality;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monorama.iot_server.domain.AirQualityData;
import com.monorama.iot_server.domain.embedded.AirQualityDataItem;

import java.time.LocalDateTime;

public record AQDHistoryItemResponseDto(

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,

        Double pm25Value,
        Double pm10Value,
        Double temperature,
        Double humidity,
        Double vocValue,
        Double co2Value,
        Double picoDeviceLatitude,
        Double picoDeviceLongitude
) {
    public static AQDHistoryItemResponseDto fromEntity(AirQualityData entity) {
        AirQualityDataItem item = entity.getAirQualityDataItem();

        return new AQDHistoryItemResponseDto(
                entity.getCreatedAt(),
                item.getPm25Value(),
                item.getPm10Value(),
                item.getTemperature(),
                item.getHumidity(),
                item.getVocValue(),
                item.getCo2Value(),
                item.getPicoDeviceLatitude(),
                item.getPicoDeviceLongitude()
        );
    }
}
