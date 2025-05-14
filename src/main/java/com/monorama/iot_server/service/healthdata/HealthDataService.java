package com.monorama.iot_server.service.healthdata;

import com.monorama.iot_server.domain.User;
import com.monorama.iot_server.dto.request.HealthDataRequestDto;
import com.monorama.iot_server.dto.request.HealthDataSyncRequestDto;
import com.monorama.iot_server.exception.CommonException;
import com.monorama.iot_server.exception.ErrorCode;
import com.monorama.iot_server.repository.HealthDataRepository;
import com.monorama.iot_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthDataService {

    private final UserRepository userRepository;
    private final HealthDataRepository healthDataRepository;

    @Transactional
    public void saveRealtime(Long userId, HealthDataRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        healthDataRepository.save(dto.toEntity(user));
    }


    @Transactional
    public void saveSync(Long userId, HealthDataSyncRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        healthDataRepository.saveAll(
                request.healthDataList().stream()
                        .map(dto -> dto.toEntity(user))
                        .toList()
        );
    }
}
