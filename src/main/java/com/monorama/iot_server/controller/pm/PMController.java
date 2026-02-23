package com.monorama.iot_server.controller.pm;

import com.monorama.iot_server.annotation.UserId;
import com.monorama.iot_server.domain.type.AirAggregateUnit;
import com.monorama.iot_server.dto.ResponseDto;
import com.monorama.iot_server.dto.request.pm.AirProjectSeriesRequestDto;
import com.monorama.iot_server.dto.request.pm.ProjectRequestDto;
import com.monorama.iot_server.dto.response.project.ProjectDetailResponseDto;
import com.monorama.iot_server.dto.response.project.ProjectListForPMResponseDto;
import com.monorama.iot_server.service.pm.PMService;
import com.monorama.iot_server.service.pm.ProjectDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pm")
public class PMController {

    private final PMService pmService;
    private final ProjectDataService projectDataService;

    @GetMapping("/projects")
    public ResponseDto<ProjectListForPMResponseDto> getProjectList(@UserId Long userId) {
        return ResponseDto.ok(pmService.getProjectList(userId));
    }

    @GetMapping("/projects/{projectId}")
    public ResponseDto<ProjectDetailResponseDto> getProjectDetail(@PathVariable Long projectId) {
        return ResponseDto.ok(pmService.getProjectDetail(projectId));
    }

    @PostMapping("/projects")
    public ResponseDto<?> saveProject(@UserId Long userId, @RequestBody ProjectRequestDto projectRequestDto) {
        return ResponseDto.created(pmService.saveProject(userId, projectRequestDto));
    }

    @GetMapping("/projects/{projectId}/series")
    public ResponseDto<?> getAirProjectSeries(
            @PathVariable Long projectId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam AirAggregateUnit unit
    ) {
        AirProjectSeriesRequestDto req = new AirProjectSeriesRequestDto(
                projectId,
                startDate,
                endDate,
                unit
        );

        return ResponseDto.ok(projectDataService.getSeries(req));
    }

}
