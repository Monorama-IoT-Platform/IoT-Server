package com.monorama.iot_server.repository;

import com.monorama.iot_server.domain.AirQualityData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

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
}