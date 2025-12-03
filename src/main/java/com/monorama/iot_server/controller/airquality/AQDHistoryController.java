package com.monorama.iot_server.controller.airquality;

import com.monorama.iot_server.annotation.UserId;
import com.monorama.iot_server.dto.ResponseDto;
import com.monorama.iot_server.dto.response.airquality.AQDHistoryListResponseDto;
import com.monorama.iot_server.service.airquality.AQDHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/air-quality-data")
public class AQDHistoryController {

    private final AQDHistoryService airQualityHistoryService;

    @GetMapping("/history")
    public ResponseDto<AQDHistoryListResponseDto> getHistory(
            @UserId Long userId,
            @RequestParam("projectId") Long projectId,
            @RequestParam("date") String date,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "100") int size
    ) {
        LocalDate localDate = LocalDate.parse(date);

        AQDHistoryListResponseDto data = airQualityHistoryService.getHistory(projectId, userId, localDate, page, size);

        return ResponseDto.ok(data);
    }
}
