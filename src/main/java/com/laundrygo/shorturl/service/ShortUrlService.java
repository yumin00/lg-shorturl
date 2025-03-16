package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

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

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(randomIndex));
        }
        return sb.toString();
    }
 }