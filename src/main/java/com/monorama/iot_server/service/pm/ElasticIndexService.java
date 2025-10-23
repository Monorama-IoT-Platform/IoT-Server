package com.monorama.iot_server.service.pm;

import com.monorama.iot_server.config.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticIndexService {

    private final ElasticsearchProperties esProps;
    private final RestTemplate restTemplate;

    public void createIndex(String indexIdentifier,
                            Map<String, Boolean> healthFields,
                            Map<String, Boolean> airFields,
                            Map<String, Boolean> personalFields) {

        String indexName = "index" + indexIdentifier; // index-{userId}-{projectId}-{endDate}

        Map<String, Object> mappings = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();

        properties.put("userId", Map.of("type", "long"));
        properties.put("timestamp", Map.of(
                "type", "date",
                "format", "strict_date_optional_time||epoch_second"
        ));

        if (healthFields != null) {
            for (String field : healthFields.keySet()) {
                switch (field) {
                    case "sleepAnalysis", "ecgData" -> properties.put(field, Map.of("type", "keyword"));
                    default -> properties.put(field, Map.of("type", "double"));
                }
            }
        }

        if (airFields != null) {
            for (String field : airFields.keySet()) {
                if (field.endsWith("Level")) {
                    properties.put(field, Map.of("type", "integer"));
                } else {
                    properties.put(field, Map.of("type", "double"));
                }
            }
        }

        if (personalFields != null) {
            for (String field : personalFields.keySet()) {
                switch (field) {
                    case "height", "weight" -> properties.put(field, Map.of("type", "double"));
                    case "dateOfBirth" -> properties.put(field, Map.of("type", "date"));
                    default -> properties.put(field, Map.of("type", "keyword"));
                }
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

    // TODO: air meta data index create method
    // TODO: 프로젝트 참여 user 별 만들어주기
    // TODO: 스케쥴러로 index 관리 (삭제)
}
