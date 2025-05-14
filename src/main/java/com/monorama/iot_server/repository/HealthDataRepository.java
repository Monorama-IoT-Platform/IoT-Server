package com.monorama.iot_server.repository;

import com.monorama.iot_server.domain.HealthData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthDataRepository extends JpaRepository<HealthData, Long> {
}
