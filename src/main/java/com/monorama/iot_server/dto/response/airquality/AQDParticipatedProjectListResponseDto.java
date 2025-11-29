package com.monorama.iot_server.dto.response.airquality;

import com.monorama.iot_server.domain.Project;

import java.util.List;

public record AQDParticipatedProjectListResponseDto(
        List<AQDParticipatedProjectResponseDto> projects
) {
    public static AQDParticipatedProjectListResponseDto fromEntities(List<Project> projects) {
        List<AQDParticipatedProjectResponseDto> list = projects.stream()
                .map(AQDParticipatedProjectResponseDto::fromEntity)
                .toList();

        return new AQDParticipatedProjectListResponseDto(list);
    }
}
