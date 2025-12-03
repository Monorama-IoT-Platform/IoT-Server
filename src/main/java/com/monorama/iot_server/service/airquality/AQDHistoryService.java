package com.monorama.iot_server.service.airquality;

import com.monorama.iot_server.domain.AirQualityData;
import com.monorama.iot_server.dto.response.airquality.AQDHistoryListResponseDto;
import com.monorama.iot_server.repository.AQDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AQDHistoryService {

    private final AQDRepository airQualityDataRepository;

    public AQDHistoryListResponseDto getHistory(Long projectId, Long userId, LocalDate date, int page, int size) {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        ZonedDateTime startZdt = date.atStartOfDay(zoneId);
        ZonedDateTime endZdt = date.plusDays(1).atStartOfDay(zoneId);

        Date start = Date.from(startZdt.toInstant());
        Date end = Date.from(endZdt.toInstant());

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        Slice<AirQualityData> slice =
                airQualityDataRepository.findHistoryByProjectAndUserAndDate(
                        projectId,
                        userId,
                        start,
                        end,
                        pageable
                );

        return AQDHistoryListResponseDto.fromSlice(slice);
    }
}
