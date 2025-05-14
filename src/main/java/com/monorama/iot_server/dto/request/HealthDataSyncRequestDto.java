package com.monorama.iot_server.dto.request;

import java.util.List;

public record HealthDataSyncRequestDto(
        List<HealthDataRequestDto> healthDataList
) {}
