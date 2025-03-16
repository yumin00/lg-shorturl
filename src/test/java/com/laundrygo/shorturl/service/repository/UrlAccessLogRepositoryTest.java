package com.laundrygo.shorturl.service.repository;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    @DisplayName("URL 매핑 ID와 시간 범위로 접근 로그 조회 테스트")
    void findAccessLogsByUrlMappingIdAndTimeRange() {
        // given
        Long urlMappingId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.minusHours(24);

        // when
        List<UrlAccessLog> accessLogs = urlAccessLogRepository.findAccessLogsByUrlMappingIdAndTimeRange(
                urlMappingId, endDate, now);

        // then
        assertThat(accessLogs).isNotEmpty();
        assertThat(accessLogs.size()).isEqualTo(28);

        // 모든 로그의 URL 매핑 ID가 1인지 확인
        assertThat(accessLogs).allMatch(log -> log.getUrlMappingId().equals(urlMappingId));

        // 모든 로그의 접근 시간이 주어진 범위 내에 있는지 확인
        assertThat(accessLogs).allMatch(log ->
                !log.getAccessedAt().isBefore(endDate) && !log.getAccessedAt().isAfter(now));
    }

    @Test
    @DisplayName("URL 매핑 ID와 시간 범위로 접근 로그 조회 - 결과 없음")
    void findAccessLogsByUrlMappingIdAndTimeRangeWithNoResults() {
        // given
        Long urlMappingId = 999L; // 존재하지 않는 URL 매핑 ID
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime onehrsAgo = now.minusHours(1);

        // when
        List<UrlAccessLog> accessLogs = urlAccessLogRepository.findAccessLogsByUrlMappingIdAndTimeRange(
                urlMappingId, onehrsAgo, now);

        // then
        assertThat(accessLogs).isEmpty();
    }

    @Test
    @DisplayName("두 번째 URL의 접근 로그 조회 테스트")
    void findAccessLogsForSecondUrl() {
        // given
        Long urlMappingId = 2L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusHours(24);

        // when
        List<UrlAccessLog> accessLogs = urlAccessLogRepository.findAccessLogsByUrlMappingIdAndTimeRange(
                urlMappingId, dayAgo, now);

        // then
        assertThat(accessLogs).isNotEmpty();
        assertThat(accessLogs.size()).isEqualTo(4);

        // 모든 로그의 URL 매핑 ID가 2인지 확인
        assertThat(accessLogs).allMatch(log -> log.getUrlMappingId().equals(urlMappingId));
    }
}