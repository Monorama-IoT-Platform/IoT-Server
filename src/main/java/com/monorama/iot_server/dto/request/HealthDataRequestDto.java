package com.monorama.iot_server.dto.request;

import com.monorama.iot_server.domain.HealthData;
import com.monorama.iot_server.domain.User;
import com.monorama.iot_server.domain.embedded.HealthDataItem;

public record HealthDataRequestDto(
        Double stepCount,
        Double runningSpeed,
        Double basalEnergyBurned,
        Double activeEnergyBurned,
        String sleepAnalysis,
        Double heartRate,
        Double oxygenSaturation,
        Double bloodPressureSystolic,
        Double bloodPressureDiastolic,
        Double respiratoryRate,
        Double bodyTemperature,
        String ecgData,
        Double watchDeviceLatitude,
        Double watchDeviceLongitude
) {
    public HealthData toEntity(User user) {
        HealthDataItem item = HealthDataItem.builder()
                .stepCount(stepCount)
                .runningSpeed(runningSpeed)
                .basalEnergyBurned(basalEnergyBurned)
                .activeEnergyBurned(activeEnergyBurned)
                .sleepAnalysis(sleepAnalysis)
                .heartRate(heartRate)
                .oxygenSaturation(oxygenSaturation)
                .bloodPressureSystolic(bloodPressureSystolic)
                .bloodPressureDiastolic(bloodPressureDiastolic)
                .respiratoryRate(respiratoryRate)
                .bodyTemperature(bodyTemperature)
                .ecgData(ecgData)
                .watchDeviceLatitude(watchDeviceLatitude)
                .watchDeviceLongitude(watchDeviceLongitude)
                .build();

        return HealthData.builder()
                .user(user)
                .healthDataItem(item)
                .build();
    }
}
