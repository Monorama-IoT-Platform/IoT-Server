package com.monorama.iot_server.domain;

import com.monorama.iot_server.domain.embedded.HealthDataItem;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "health_data_tb")
@NoArgsConstructor
public class HealthData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_data_id")
    private Long id;

    /*** basic information ***/
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME(0)")
    private Date createdAt;

    @Embedded
    private HealthDataItem healthDataItem;

    /*** mapping information ***/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*** business logic ***/
    @Builder
    public HealthData(Long id, Date createdAt, HealthDataItem healthDataItem, User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.healthDataItem = healthDataItem;
        this.user = user;
    }
}
