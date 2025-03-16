package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final UrlMappingRepository urlMappingRepository;
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

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(randomIndex));
        }
        return sb.toString();
    }
 }