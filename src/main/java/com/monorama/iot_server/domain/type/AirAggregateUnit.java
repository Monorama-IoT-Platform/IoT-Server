package com.monorama.iot_server.domain.type;

public enum AirAggregateUnit {
    SECOND(1),
    MINUTE(60),
    THIRTY_MINUTES(1800),
    HOUR(3600);

    private final int stepSec;

    AirAggregateUnit(int stepSec) {
        this.stepSec = stepSec;
    }

    public int stepSec() {
        return stepSec;
    }
}
