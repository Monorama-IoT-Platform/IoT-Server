package com.monorama.iot_server.service.airquality;

import com.monorama.iot_server.domain.AirQualityData;
import com.monorama.iot_server.domain.User;
import com.monorama.iot_server.dto.request.airquality.AQDRequestDto;
import com.monorama.iot_server.dto.request.airquality.AQDSyncRequestDto;
import com.monorama.iot_server.exception.CommonException;
import com.monorama.iot_server.exception.ErrorCode;
import com.monorama.iot_server.repository.AQDRepository;
import com.monorama.iot_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AQDService {

    private final UserRepository userRepo;
    private final AQDRepository airRepo;

    @Transactional
    public String saveRealtime(Long userId, AQDRequestDto dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        log.info("before make Entity : {}", dto.createdAt());
        List<String> missingFields = dto.getMissingFields(user);

        AirQualityData result = dto.toEntity(user);
        log.info("after make Entity : {}", result.getCreatedAt());
        airRepo.save(dto.toEntity(user));

        return missingFields.isEmpty() ?
                "모든 필드가 정상적으로 저장되었습니다." :
                "누락된 필드: " + missingFields;
    }



    @Transactional
    public void saveSync(Long userId, AQDSyncRequestDto request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        airRepo.saveAll(
                request.airQualityDataList().stream()
                        .map(dto -> dto.toEntity(user))
                        .toList()
        );
    }
}
