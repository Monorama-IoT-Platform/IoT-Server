package com.monorama.iot_server.repository.projection;

import java.time.LocalDateTime;

public interface AirBucketAvgRow {
    LocalDateTime getBucketTime();

    Double getPm25ValueAvg();
    Double getPm25LevelAvg();

    Double getPm10ValueAvg();
    Double getPm10LevelAvg();

    Double getTemperatureAvg();
    Double getTemperatureLevelAvg();

    Double getHumidityAvg();
    Double getHumidityLevelAvg();

    Double getCo2ValueAvg();
    Double getCo2LevelAvg();

    Double getVocValueAvg();
    Double getVocLevelAvg();

    Double getPicoLatAvg();
    Double getPicoLonAvg();
}
