package com.monorama.iot_server.service.pm;

import com.monorama.iot_server.domain.type.ProjectType;
import com.monorama.iot_server.dto.request.pm.ProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PMProjectApplicationService {

    private final PMService pmService;
    private final ElasticIndexService elasticIndexService;

    public void createProjectWithIndex(Long pmId, ProjectRequestDto requestDto) {
        String indexIdentifier = pmService.saveProject(pmId, requestDto);

        ProjectType projectType = requestDto.projectType();
        Map<String, Boolean> healthFields = extractHealthFields(requestDto);
        Map<String, Boolean> airFields = extractAirFields(requestDto);
        Map<String, Boolean> personalFields = extractPersonalFields(requestDto);

        if (projectType == ProjectType.HEALTH_DATA) {
            airFields = null;
        } else if (projectType == ProjectType.AIR_QUALITY) {
            healthFields = null;
        }

        // TODO: create index 실패시 생성된 프로젝트도 삭제
        elasticIndexService.createIndex(indexIdentifier, healthFields, airFields, personalFields);
    }

    private Map<String, Boolean> extractHealthFields(ProjectRequestDto dto) {
        Map<String, Boolean> fields = new HashMap<>();
        if (dto.stepCount()) fields.put("stepCount", true);
        if (dto.runningSpeed()) fields.put("runningSpeed", true);
        if (dto.basalEnergyBurned()) fields.put("basalEnergyBurned", true);
        if (dto.activeEnergyBurned()) fields.put("activeEnergyBurned", true);
        if (dto.sleepAnalysis()) fields.put("sleepAnalysis", true);
        if (dto.heartRate()) fields.put("heartRate", true);
        if (dto.oxygenSaturation()) fields.put("oxygenSaturation", true);
        if (dto.bloodPressureSystolic()) fields.put("bloodPressureSystolic", true);
        if (dto.bloodPressureDiastolic()) fields.put("bloodPressureDiastolic", true);
        if (dto.respiratoryRate()) fields.put("respiratoryRate", true);
        if (dto.bodyTemperature()) fields.put("bodyTemperature", true);
        if (dto.ecgData()) fields.put("ecgData", true);
        if (dto.watchDeviceLatitude()) fields.put("watchDeviceLatitude", true);
        if (dto.watchDeviceLongitude()) fields.put("watchDeviceLongitude", true);
        return fields;
    }

    private Map<String, Boolean> extractAirFields(ProjectRequestDto dto) {
        Map<String, Boolean> fields = new HashMap<>();
        if (dto.pm25Value()) fields.put("pm25Value", true);
        if (dto.pm25Level()) fields.put("pm25Level", true);
        if (dto.pm10Value()) fields.put("pm10Value", true);
        if (dto.pm10Level()) fields.put("pm10Level", true);
        if (dto.temperature()) fields.put("temperature", true);
        if (dto.temperatureLevel()) fields.put("temperatureLevel", true);
        if (dto.humidity()) fields.put("humidity", true);
        if (dto.humidityLevel()) fields.put("humidityLevel", true);
        if (dto.co2Value()) fields.put("co2Value", true);
        if (dto.co2Level()) fields.put("co2Level", true);
        if (dto.vocValue()) fields.put("vocValue", true);
        if (dto.vocLevel()) fields.put("vocLevel", true);
        if (dto.picoDeviceLatitude()) fields.put("picoDeviceLatitude", true);
        if (dto.picoDeviceLongitude()) fields.put("picoDeviceLongitude", true);
        return fields;
    }

    private Map<String, Boolean> extractPersonalFields(ProjectRequestDto dto) {
        Map<String, Boolean> fields = new HashMap<>();
        if (dto.name()) fields.put("name", true);
        if (dto.email()) fields.put("email", true);
        if (dto.gender()) fields.put("gender", true);
        if (dto.nationalCode()) fields.put("nationalCode", true);
        if (dto.phoneNumber()) fields.put("phoneNumber", true);
        if (dto.dateOfBirth()) fields.put("dateOfBirth", true);
        if (dto.bloodType()) fields.put("bloodType", true);
        if (dto.weight()) fields.put("weight", true);
        if (dto.height()) fields.put("height", true);
        return fields;
    }

    //TODO: 생각해보니 맵으로 할 이유가 전혀 없음. 리스트로 처리
}
