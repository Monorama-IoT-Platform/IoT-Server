package com.monorama.iot_server.controller.healthdata;

import com.monorama.iot_server.annotation.UserId;
import com.monorama.iot_server.dto.ResponseDto;
import com.monorama.iot_server.dto.request.HealthDataRequestDto;
import com.monorama.iot_server.dto.request.HealthDataSyncRequestDto;
import com.monorama.iot_server.service.healthdata.HealthDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/health-data")
@RequiredArgsConstructor
public class HealthDataController {

    private final HealthDataService service;

    @PostMapping("/realtime")
    public ResponseDto<?> saveRealtimeHealthData(
            @UserId Long userId,
            @RequestBody @Valid HealthDataRequestDto request
    ) {
        service.saveRealtime(userId, request);
        return ResponseDto.ok(null);
    }

    @PostMapping("/sync")
    public ResponseDto<?> saveSyncHealthData(
            @UserId Long userId,
            @RequestBody @Valid HealthDataSyncRequestDto request
    ) {
        service.saveSync(userId, request);
        return ResponseDto.ok(null);
    }
}
