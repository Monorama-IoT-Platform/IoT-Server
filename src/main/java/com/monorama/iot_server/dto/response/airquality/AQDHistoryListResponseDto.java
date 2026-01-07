package com.monorama.iot_server.dto.response.airquality;

import com.monorama.iot_server.domain.AirQualityData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record AQDHistoryListResponseDto(
        List<AQDHistoryItemResponseDto> historyList,
        boolean hasNext
) {
    public static AQDHistoryListResponseDto fromSlice(Slice<AirQualityData> slice) {
        List<AQDHistoryItemResponseDto> list = slice.getContent().stream()
                .map(AQDHistoryItemResponseDto::fromEntity)
                .toList();

        return new AQDHistoryListResponseDto(list, slice.hasNext());
    }
}
