package com.monorama.iot_server.dto.request.pm;

import com.monorama.iot_server.domain.type.AirAggregateUnit;
import java.time.LocalDateTime;

public record AirProjectSeriesRequestDto(
        Long projectId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        AirAggregateUnit unit
) {

}