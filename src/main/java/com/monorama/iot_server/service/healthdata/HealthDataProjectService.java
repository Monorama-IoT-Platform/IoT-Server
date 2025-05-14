package com.monorama.iot_server.service.healthdata;

import com.monorama.iot_server.domain.Project;
import com.monorama.iot_server.domain.User;
import com.monorama.iot_server.domain.UserDataPermission;
import com.monorama.iot_server.domain.UserProject;
import com.monorama.iot_server.domain.type.ProjectType;
import com.monorama.iot_server.domain.type.TermsType;
import com.monorama.iot_server.dto.response.project.ProjectDetailResponseDto;
import com.monorama.iot_server.dto.response.project.ProjectListResponseDto;
import com.monorama.iot_server.dto.response.project.ProjectSimpleResponseDto;
import com.monorama.iot_server.dto.response.terms.TermsContentResponseDto;
import com.monorama.iot_server.exception.CommonException;
import com.monorama.iot_server.repository.ProjectRepository;
import com.monorama.iot_server.repository.UserProjectRepository;
import com.monorama.iot_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.monorama.iot_server.exception.ErrorCode;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthDataProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;

    public ProjectListResponseDto getAvailableHealthProjectList() {
        Date now = new Date();

        List<ProjectSimpleResponseDto> projects = projectRepository.findAll().stream()
                .filter(project ->
                        (project.getProjectType() == ProjectType.HEALTH_DATA || project.getProjectType() == ProjectType.BOTH) &&
                                !project.getStartDate().after(now) &&
                                !project.getEndDate().before(now)
                )
                .map(ProjectSimpleResponseDto::fromEntity)
                .toList();

        return ProjectListResponseDto.of(projects);
    }

    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_PROJECT));
        return ProjectDetailResponseDto.fromEntity(project);
    }

    public TermsContentResponseDto getTermsContent(Long projectId, TermsType type) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));

        return TermsContentResponseDto.fromEntity(project, type);
    }


    @Transactional
    public void participateProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_PROJECT));

        Date now = new Date();
        if (project.getStartDate().after(now) || project.getEndDate().before(now)) {
            throw new CommonException(ErrorCode.PROJECT_NOT_AVAILABLE);
        }

        if (userProjectRepository.existsByUserAndProject(user, project)) {
            throw new CommonException(ErrorCode.ALREADY_JOINED_PROJECT);
        }

        // 유저-프로젝트 참여 등록
        UserProject userProject = UserProject.create(user, project);
        userProjectRepository.save(userProject);

        // 유저 권한 정보 업데이트
        UserDataPermission userDataPermission = user.getUserDataPermission();
        userDataPermission.getAirQualityDataFlag().updateBy(project.getAirQualityDataFlag());
        userDataPermission.getHealthDataFlag().updateBy(project.getHealthDataFlag());
        userDataPermission.getPersonalInfoFlag().updateBy(project.getPersonalInfoFlag());
    }


}