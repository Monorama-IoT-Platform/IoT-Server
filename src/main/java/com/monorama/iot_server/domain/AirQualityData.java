package com.monorama.iot_server.domain;

import com.monorama.iot_server.domain.embedded.AirQualityDataItem;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "air_quality_data_tb")
public class AirQualityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_quality_data_id")
    private Long id;

    /*** basic information ***/
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Embedded
    private AirQualityDataItem airQualityDataItem;

    /*** mapping information ***/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*** business logic ***/
    public void setUser(User user) {
        this.user = user;
        user.getAirQualityDataList().add(this);
    }

    public void setAirQualityDataItem(AirQualityDataItem item) {
        this.airQualityDataItem = item;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
