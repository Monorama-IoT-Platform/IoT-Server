package com.monorama.iot_server.repository;

import com.monorama.iot_server.domain.AirQualityData;
import com.monorama.iot_server.repository.projection.AirBucketAvgRow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AQDRepository extends JpaRepository<AirQualityData, Long> {
    @Query("""
        SELECT a
        FROM AirQualityData a
            JOIN a.user u
            JOIN UserProject up ON up.user = u
        WHERE up.project.id = :projectId
          AND u.id = :userId
          AND a.createdAt >= :start
          AND a.createdAt < :end
        ORDER BY a.createdAt desc
        """)
    Slice<AirQualityData> findHistoryByProjectAndUserAndDate(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );


    @Query(value = """
SELECT
  FROM_UNIXTIME(t.bucketTs) AS bucketTime,
  AVG(CASE WHEN :pm25ValueFlag = 1 THEN t.pm25_value END) AS pm25ValueAvg,
  AVG(CASE WHEN :pm25LevelFlag = 1 THEN t.pm25_level END) AS pm25LevelAvg,
  AVG(CASE WHEN :pm10ValueFlag = 1 THEN t.pm10_value END) AS pm10ValueAvg,
  AVG(CASE WHEN :pm10LevelFlag = 1 THEN t.pm10_level END) AS pm10LevelAvg,
  AVG(CASE WHEN :temperatureFlag = 1 THEN t.temperature END) AS temperatureAvg,
  AVG(CASE WHEN :temperatureLevelFlag = 1 THEN t.temperature_level END) AS temperatureLevelAvg,
  AVG(CASE WHEN :humidityFlag = 1 THEN t.humidity END) AS humidityAvg,
  AVG(CASE WHEN :humidityLevelFlag = 1 THEN t.humidity_level END) AS humidityLevelAvg,
  AVG(CASE WHEN :co2ValueFlag = 1 THEN t.co2_value END) AS co2ValueAvg,
  AVG(CASE WHEN :co2LevelFlag = 1 THEN t.co2_level END) AS co2LevelAvg,
  AVG(CASE WHEN :vocValueFlag = 1 THEN t.voc_value END) AS vocValueAvg,
  AVG(CASE WHEN :vocLevelFlag = 1 THEN t.voc_level END) AS vocLevelAvg,
  AVG(CASE WHEN :picoLatFlag = 1 THEN t.pico_device_latitude END) AS picoLatAvg,
  AVG(CASE WHEN :picoLonFlag = 1 THEN t.pico_device_longitude END) AS picoLonAvg
FROM (
  SELECT
    UNIX_TIMESTAMP(:rangeStart) + ((UNIX_TIMESTAMP(aqd.created_at) - UNIX_TIMESTAMP(:rangeStart)) DIV :stepSec) * :stepSec AS bucketTs,
    aqd.pm25_value,
    aqd.pm25_level,
    aqd.pm10_value,
    aqd.pm10_level,
    aqd.temperature,
    aqd.temperature_level,
    aqd.humidity,
    aqd.humidity_level,
    aqd.co2_value,
    aqd.co2_level,
    aqd.voc_value,
    aqd.voc_level,
    aqd.pico_device_latitude,
    aqd.pico_device_longitude
  FROM user_project_tb up
  JOIN air_quality_data_tb aqd
    ON aqd.user_id = up.user_id
  WHERE up.project_id = :projectId
    AND aqd.created_at >= GREATEST(:rangeStart, up.created_at)
    AND aqd.created_at < :rangeEnd
) t
GROUP BY t.bucketTs
ORDER BY t.bucketTs ASC
""", nativeQuery = true)
    List<AirBucketAvgRow> findAveragedSeriesByProject(
            @Param("projectId") Long projectId,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("stepSec") Integer stepSec,

            @Param("pm25ValueFlag") Integer pm25ValueFlag,
            @Param("pm25LevelFlag") Integer pm25LevelFlag,
            @Param("pm10ValueFlag") Integer pm10ValueFlag,
            @Param("pm10LevelFlag") Integer pm10LevelFlag,
            @Param("temperatureFlag") Integer temperatureFlag,
            @Param("temperatureLevelFlag") Integer temperatureLevelFlag,
            @Param("humidityFlag") Integer humidityFlag,
            @Param("humidityLevelFlag") Integer humidityLevelFlag,
            @Param("co2ValueFlag") Integer co2ValueFlag,
            @Param("co2LevelFlag") Integer co2LevelFlag,
            @Param("vocValueFlag") Integer vocValueFlag,
            @Param("vocLevelFlag") Integer vocLevelFlag,
            @Param("picoLatFlag") Integer picoLatFlag,
            @Param("picoLonFlag") Integer picoLonFlag
    );
}