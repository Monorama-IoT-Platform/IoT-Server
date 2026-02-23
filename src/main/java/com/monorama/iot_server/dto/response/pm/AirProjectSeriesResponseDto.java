package com.monorama.iot_server.dto.response.pm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monorama.iot_server.domain.embedded.AirQualityDataFlag;
import com.monorama.iot_server.repository.projection.AirBucketAvgRow;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record AirProjectSeriesResponseDto(
        Long projectId,
        LocalDateTime rangeStart,
        LocalDateTime rangeEnd,
        Integer stepSec,
        Integer totalBucketCount,
        EnabledMetrics enabledMetrics,
        List<Point> points
) {

    public static AirProjectSeriesResponseDto from(
            Long projectId,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime rangeStart,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime rangeEnd,
            Integer stepSec,
            AirQualityDataFlag flag,
            List<AirBucketAvgRow> rows
    ) {
        EnabledMetrics enabledMetrics = EnabledMetrics.fromFlag(flag);
        List<Point> points = rows.stream()
                .map(r -> Point.from(flag, r))
                .toList();

        return new AirProjectSeriesResponseDto(
                projectId,
                rangeStart,
                rangeEnd,
                stepSec,
                rows.size(),
                enabledMetrics,
                points
        );
    }

    public record EnabledMetrics(
            Boolean pm25Value,
            Boolean pm25Level,
            Boolean pm10Value,
            Boolean pm10Level,
            Boolean temperature,
            Boolean temperatureLevel,
            Boolean humidity,
            Boolean humidityLevel,
            Boolean co2Value,
            Boolean co2Level,
            Boolean vocValue,
            Boolean vocLevel,
            Boolean picoDeviceLatitude,
            Boolean picoDeviceLongitude
    ) {
        public static EnabledMetrics fromFlag(AirQualityDataFlag flag) {
            return new EnabledMetrics(
                    flag.getPm25Value(),
                    flag.getPm25Level(),
                    flag.getPm10Value(),
                    flag.getPm10Level(),
                    flag.getTemperature(),
                    flag.getTemperatureLevel(),
                    flag.getHumidity(),
                    flag.getHumidityLevel(),
                    flag.getCo2Value(),
                    flag.getCo2Level(),
                    flag.getVocValue(),
                    flag.getVocLevel(),
                    flag.getPicoDeviceLatitude(),
                    flag.getPicoDeviceLongitude()
            );
        }
    }

    public record Point(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime time,
            Map<String, Double> metrics
    ) {
        public static Point from(AirQualityDataFlag flag, AirBucketAvgRow r) {
            return new Point(r.getBucketTime(), buildMetrics(flag, r));
        }

        private static Map<String, Double> buildMetrics(AirQualityDataFlag flag, AirBucketAvgRow r) {
            Map<String, Double> m = new LinkedHashMap<>();

            put(m, "pm25ValueAvg", flag.getPm25Value(), r.getPm25ValueAvg());
            put(m, "pm25LevelAvg", flag.getPm25Level(), r.getPm25LevelAvg());

            put(m, "pm10ValueAvg", flag.getPm10Value(), r.getPm10ValueAvg());
            put(m, "pm10LevelAvg", flag.getPm10Level(), r.getPm10LevelAvg());

            put(m, "temperatureAvg", flag.getTemperature(), r.getTemperatureAvg());
            put(m, "temperatureLevelAvg", flag.getTemperatureLevel(), r.getTemperatureLevelAvg());

            put(m, "humidityAvg", flag.getHumidity(), r.getHumidityAvg());
            put(m, "humidityLevelAvg", flag.getHumidityLevel(), r.getHumidityLevelAvg());

            put(m, "co2ValueAvg", flag.getCo2Value(), r.getCo2ValueAvg());
            put(m, "co2LevelAvg", flag.getCo2Level(), r.getCo2LevelAvg());

            put(m, "vocValueAvg", flag.getVocValue(), r.getVocValueAvg());
            put(m, "vocLevelAvg", flag.getVocLevel(), r.getVocLevelAvg());

            put(m, "picoLatAvg", flag.getPicoDeviceLatitude(), r.getPicoLatAvg());
            put(m, "picoLonAvg", flag.getPicoDeviceLongitude(), r.getPicoLonAvg());

            return m;
        }

        private static void put(Map<String, Double> m, String key, Boolean enabled, Double value) {
            if (Boolean.TRUE.equals(enabled) && value != null) {
                m.put(key, value);
            }
        }
    }
}