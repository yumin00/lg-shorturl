package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.HourlyAccessStats;
import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.domain.UrlStats;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final UrlMappingRepository urlMappingRepository;
    private final UrlAccessLogRepository urlAccessLogRepository;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String ALPHABET = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SHORT_URL_LENGTH = 8;

    public String shortenUrl(String originalUrl) {
        // 이미 존재하는지 확인
        UrlMapping existingMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
        if (existingMapping != null) {
            return existingMapping.getShortUrl();
        }

        // 새 단축 URL 생성
        String shortUrl;
        do {
            shortUrl = generateRandomString(SHORT_URL_LENGTH);
        } while (urlMappingRepository.existsByShortUrl(shortUrl));

        // 저장
        urlMappingRepository.save(UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .build());

        return shortUrl;
    }

    public String getOriginalUrlAndCreateAccessLog(String shortUrl) {
        // 원본 URL 조회
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            return null;
        }

        // 로그 생성
        urlAccessLogRepository.save(UrlAccessLog.builder()
                .urlMappingId(urlMapping.getId())
                .accessedAt(LocalDateTime.now())
                .build());

        return urlMapping.getOriginalUrl();
    }

    public UrlStats getShortUrlAccessStats(String shortUrl) {
        // 원본 URL 조회
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            return null;
        }

        // 시간 당 응답 수 조회
        List<HourlyAccessStats> hourlyAccessStats = this.getHourlyAccessStats(urlMapping.getId());

        return UrlStats.builder()
                .originalUrl(urlMapping.getOriginalUrl())
                .hourlyStats(hourlyAccessStats)
                .build();
    }

    public List<HourlyAccessStats> getHourlyAccessStats(Long urlMappingId) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(23);

        // 로그 데이터 조회
        List<UrlAccessLog> accessLogs = urlAccessLogRepository
                .findAccessLogsByUrlMappingIdAndTimeRange(urlMappingId, startTime, endTime);

        Map<LocalDateTime, Long> hourlyCountMap = new TreeMap<>();

        LocalDateTime current = startTime.truncatedTo(ChronoUnit.HOURS);
        while (current.isBefore(endTime) || current.equals(endTime)) {
            hourlyCountMap.put(current, 0L);
            current = current.plusHours(1);
        }

        // 로그에서 시간별 카운트 계산
        accessLogs.stream()
                .map(log -> log.getAccessedAt().truncatedTo(ChronoUnit.HOURS))
                .forEach(hourKey -> hourlyCountMap.compute(hourKey, (k, v) -> v + 1));

        // 결과 리스트로 변환
        return hourlyCountMap.entrySet().stream()
                .map(entry -> new HourlyAccessStats(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(randomIndex));
        }
        return sb.toString();
    }
 }