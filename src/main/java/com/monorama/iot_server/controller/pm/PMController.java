package com.monorama.iot_server.controller.pm;

import com.monorama.iot_server.annotation.UserId;
import com.monorama.iot_server.dto.ResponseDto;
import com.monorama.iot_server.dto.request.pm.ProjectRequestDto;
import com.monorama.iot_server.dto.response.project.ProjectDetailResponseDto;
import com.monorama.iot_server.dto.response.project.ProjectListForPMResponseDto;
import com.monorama.iot_server.service.pm.PMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pm")
public class PMController {

    private final PMService pmService;

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

}
