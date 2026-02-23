package com.monorama.iot_server.service.pm;

import com.monorama.iot_server.domain.Project;
import com.monorama.iot_server.domain.embedded.AirQualityDataFlag;
import com.monorama.iot_server.dto.request.pm.AirProjectSeriesRequestDto;
import com.monorama.iot_server.dto.response.pm.AirProjectSeriesResponseDto;
import com.monorama.iot_server.repository.AQDRepository;
import com.monorama.iot_server.repository.ProjectRepository;
import com.monorama.iot_server.repository.projection.AirBucketAvgRow;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectDataService {

    private final ProjectRepository projectRepository;
    private final AQDRepository aqdRepository;

    @Transactional(readOnly = true)
    public AirProjectSeriesResponseDto getSeries(AirProjectSeriesRequestDto req) {

        Project project = projectRepository.findById(req.projectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found. id=" + req.projectId()));

        AirQualityDataFlag flag = project.getAirQualityDataFlag();

        LocalDateTime rangeStart = req.startDate();
        LocalDateTime rangeEnd = req.endDate();
        Integer stepSec = req.unit().stepSec();

        log.info("range start: {}, end: {}", rangeStart, rangeEnd);
        log.info("stepSec: {}, projcetId: {}", stepSec,  req.projectId());

        List<AirBucketAvgRow> rows = aqdRepository.findAveragedSeriesByProject(
                req.projectId(),
                rangeStart,
                rangeEnd,
                stepSec,
                boolToInt(flag.getPm25Value()),
                boolToInt(flag.getPm25Level()),
                boolToInt(flag.getPm10Value()),
                boolToInt(flag.getPm10Level()),
                boolToInt(flag.getTemperature()),
                boolToInt(flag.getTemperatureLevel()),
                boolToInt(flag.getHumidity()),
                boolToInt(flag.getHumidityLevel()),
                boolToInt(flag.getCo2Value()),
                boolToInt(flag.getCo2Level()),
                boolToInt(flag.getVocValue()),
                boolToInt(flag.getVocLevel()),
                boolToInt(flag.getPicoDeviceLatitude()),
                boolToInt(flag.getPicoDeviceLongitude())
        );

        rows.forEach(row -> {
            System.out.println(row.getBucketTime());
        });
        log.info("list size: {}", rows.size());

        return AirProjectSeriesResponseDto.from(
                req.projectId(),
                rangeStart,
                rangeEnd,
                stepSec,
                flag,
                rows
        );
    }

    private Integer boolToInt(Boolean v) {
        return Boolean.TRUE.equals(v) ? 1 : 0;
    }
}