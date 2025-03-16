package com.laundrygo.shorturl.service.repository;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UrlAccessLogRepositoryTest {

    @Autowired
    private UrlAccessLogRepository urlAccessLogRepository;

    @Test
    @DisplayName("URL 접근 로그 저장 테스트")
    void saveUrlAccessLog() {
        // given
        LocalDateTime now = LocalDateTime.now();
        UrlAccessLog accessLog = UrlAccessLog.builder()
                .urlMappingId(1L)
                .accessedAt(now)
                .build();

        // when
        urlAccessLogRepository.save(accessLog);

        // then
        // 저장 후 ID가 생성되었는지 확인
        assertThat(accessLog.getId()).isNotNull();
    }
}