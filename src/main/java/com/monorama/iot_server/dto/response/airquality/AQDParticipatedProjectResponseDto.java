package com.monorama.iot_server.dto.response.airquality;

import com.monorama.iot_server.domain.Project;

public record AQDParticipatedProjectResponseDto(
        Long projectId,
        String title
) {
    public static AQDParticipatedProjectResponseDto fromEntity(Project project) {
        return new AQDParticipatedProjectResponseDto(
                project.getId(),
                project.getTitle()
        );
    }
}
