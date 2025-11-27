package com.monorama.iot_server.service.pm;

import com.monorama.iot_server.config.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ElasticIndexService {

    private final ElasticsearchProperties esProps;
    private final RestTemplate restTemplate;

    public void createHealthIndex(String indexIdentifier) {

        String indexName = "index-health" + indexIdentifier; // index-health-{userId}-{projectId}-{endDate}

        Map<String, Object> mappings = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        List<String> healthFields = extractHealthFields();
        List<String> personalFields = extractPersonalFields();

        properties.put("userId", Map.of("type", "long"));
        properties.put("timestamp", Map.of(
                "type", "date",
                "format", "strict_date_optional_time||epoch_second"
        ));


        for (String field : healthFields) {
            switch (field) {
                case "sleepAnalysis", "ecgData", "title" -> properties.put(field, Map.of("type", "keyword"));
                default -> properties.put(field, Map.of("type", "double"));
            }
        }


        for (String field : personalFields) {
            switch (field) {
                case "height", "weight" -> properties.put(field, Map.of("type", "double"));
                case "dateOfBirth" -> properties.put(field, Map.of("type", "date"));
                default -> properties.put(field, Map.of("type", "keyword"));
            }
        }

        mappings.put("properties", properties);
        Map<String, Object> payload = Map.of("mappings", mappings);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        setBasicAuthHeader(headers);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        String esUrl = esProps.getUrl() + "/" + indexName;
        ResponseEntity<String> response = restTemplate.exchange(esUrl, HttpMethod.PUT, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Index creation failed: " + response.getBody());
        }
    }

    public void createAirIndex(String indexIdentifier) {

        String indexName = "index-air" + indexIdentifier; // index-air-{userId}-{projectId}-{endDate}

        Map<String, Object> mappings = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        List<String> airFields = extractAirFields();
        List<String> personalFields = extractPersonalFields();

        properties.put("userId", Map.of("type", "long"));
        properties.put("timestamp", Map.of(
                "type", "date",
                "format", "strict_date_optional_time||epoch_second"
        ));

        for (String field : airFields) {
            if (field.endsWith("Level")) {
                properties.put(field, Map.of("type", "integer"));
            } else {
                properties.put(field, Map.of("type", "double"));
            }
        }

        for (String field : personalFields) {
            switch (field) {
                case "height", "weight" -> properties.put(field, Map.of("type", "double"));
                case "dateOfBirth" -> properties.put(field, Map.of("type", "date"));
                default -> properties.put(field, Map.of("type", "keyword"));
            }
        }

        mappings.put("properties", properties);
        Map<String, Object> payload = Map.of("mappings", mappings);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        setBasicAuthHeader(headers);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        String esUrl = esProps.getUrl() + "/" + indexName;
        ResponseEntity<String> response = restTemplate.exchange(esUrl, HttpMethod.PUT, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Index creation failed: " + response.getBody());
        }
    }

    private void setBasicAuthHeader(HttpHeaders headers) {
        String auth = esProps.getUsername() + ":" + esProps.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);
    }

    // index field 메서드

    private List<String> extractHealthFields() {
        return List.of(
                "title",
                "stepCount",
                "runningSpeed",
                "basalEnergyBurned",
                "activeEnergyBurned",
                "sleepAnalysis",
                "heartRate",
                "oxygenSaturation",
                "bloodPressureSystolic",
                "bloodPressureDiastolic",
                "respiratoryRate",
                "bodyTemperature",
                "ecgData",
                "watchDeviceLatitude",
                "watchDeviceLongitude"
        );
    }


    private List<String> extractAirFields() {
        return List.of(
                "title",
                "pm25Value",
                "pm25Level",
                "pm10Value",
                "pm10Level",
                "temperature",
                "temperatureLevel",
                "humidity",
                "humidityLevel",
                "co2Value",
                "co2Level",
                "vocValue",
                "vocLevel",
                "picoDeviceLatitude",
                "picoDeviceLongitude"
        );
    }

    private List<String> extractPersonalFields() {
        return List.of(
                "name",
                "email",
                "gender",
                "nationalCode",
                "phoneNumber",
                "dateOfBirth",
                "bloodType",
                "weight",
                "height"
        );
    }

    private List<String> extractPersonalFLagFields() {
        return List.of(
                "nameFlag",
                "emailFlag",
                "genderFlag",
                "nationalCodeFlag",
                "phoneNumberFlag",
                "dateOfBirthFlag",
                "bloodTypeFlag",
                "heightFlag",
                "weightFlag"
        );
    }

    private List<String> extractHealthFlagFields() {
        return List.of(
                "stepCountFlag",
                "runningSpeedFlag",
                "basalEnergyBurnedFlag",
                "activeEnergyBurnedFlag",
                "sleepAnalysisFlag",
                "heartRateFlag",
                "oxygenSaturationFlag",
                "bloodPressureSystolicFlag",
                "bloodPressureDiastolicFlag",
                "respiratoryRateFlag",
                "bodyTemperatureFlag",
                "ecgDataFlag",
                "watchDeviceLatitudeFlag",
                "watchDeviceLongitudeFlag"
        );
    }

    private List<String> extractAirFlagFields() {
        return List.of(
                "pm25ValueFlag",
                "pm25LevelFlag",
                "pm10ValueFlag",
                "pm10LevelFlag",
                "temperatureFlag",
                "temperatureLevelFlag",
                "humidityFlag",
                "humidityLevelFlag",
                "co2ValueFlag",
                "co2LevelFlag",
                "vocValueFlag",
                "vocLevelFlag",
                "picoDeviceLatitudeFlag",
                "picoDeviceLongitudeFlag"
        );
    }



    // TODO: air meta data index create method
    // TODO: 프로젝트 참여 user 별 만들어주기
    // TODO: 스케쥴러로 index 관리 (삭제)
}
