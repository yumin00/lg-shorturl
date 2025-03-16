package com.laundrygo.shorturl.service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.dto.request.ShortenUrlRequest;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ShortUrlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private UrlAccessLogRepository urlAccessLogRepository;

    @Test
    @DisplayName("URL 단축 통합 테스트 - 새로운 URL")
    public void shortenNewUrlIntegrationTest() throws Exception {
        // given
        String originalUrl = "https://www.integration-test.com";
        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl(originalUrl);

        // when
        MvcResult result = mockMvc.perform(post("/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").exists())
                .andReturn();

        // then
        Map<String, String> responseMap = objectMapper.readValue(
                result.getResponse().getContentAsString(), Map.class);
        String shortUrl = responseMap.get("shortUrl");

        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        assertThat(urlMapping).isNotNull();
        assertThat(urlMapping.getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test
    @DisplayName("URL 단축 통합 테스트 - 이미 존재하는 URL")
    public void shortenExistingUrlIntegrationTest() throws Exception {
        // given
        String originalUrl = "https://www.example.com";
        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl(originalUrl);

        // when
        MvcResult result = mockMvc.perform(post("/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").exists())
                .andReturn();

        // then
        Map<String, String> responseMap = objectMapper.readValue(
                result.getResponse().getContentAsString(), Map.class);

        assertThat(responseMap.get("shortUrl")).isEqualTo("example");
    }

    @Test
    @DisplayName("URL 리다이렉트 통합 테스트")
    public void redirectToOriginalUrlIntegrationTest() throws Exception {
        // given
        String shortUrl = "example";

        // when & then
        mockMvc.perform(get("/short-urls/{shortUrl}/redirect", shortUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "https://www.example.com"));

        // 접근 로그가 생성되었는지 확인
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minusMinutes(1);

        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        // 접근 로그 확인
        assertThat(urlAccessLogRepository.findAccessLogsByUrlMappingIdAndTimeRange(
                urlMapping.getId(), oneMinuteAgo, now).size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("존재하지 않는 URL 리다이렉트 테스트")
    public void redirectToNonExistentUrlIntegrationTest() throws Exception {
        // given
        String nonExistentShortUrl = "nonexistent";

        // when & then
        mockMvc.perform(get("/short-urls/{shortUrl}/redirect", nonExistentShortUrl))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("URL 통계 조회 통합 테스트")
    public void getUrlStatsIntegrationTest() throws Exception {
        // given
        String shortUrl = "example";

        // 접근 로그 추가 생성
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        UrlAccessLog accessLog = UrlAccessLog.builder()
                .urlMappingId(urlMapping.getId())
                .accessedAt(LocalDateTime.now())
                .build();

        urlAccessLogRepository.save(accessLog);

        // when
        MvcResult result = mockMvc.perform(get("/short-urls/{shortUrl}/access-stats", shortUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").exists())
                .andExpect(jsonPath("$.hourlyStats").isArray())
                .andReturn();

        // then
        Map<String, Object> responseMap = objectMapper.readValue(
                result.getResponse().getContentAsString(), Map.class);

        assertThat(responseMap.get("originalUrl")).isEqualTo("https://www.example.com");
        assertThat(responseMap.get("hourlyStats")).isNotNull();
    }

    @Test
    @DisplayName("URL 통계 조회 - 존재하지 않는 URL 테스트")
    public void getUrlStatsForNonExistentUrlIntegrationTest() throws Exception {
        // given
        String nonExistentShortUrl = "nonexistent";

        // when & then
        mockMvc.perform(get("/short-urls/{shortUrl}/access-stats", nonExistentShortUrl))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("전체 프로세스 통합 테스트 - 단축, 접근, 통계 조회")
    public void fullProcessIntegrationTest() throws Exception {
        // 1. URL 단축
        String originalUrl = "https://www.full-process-test.com";
        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl(originalUrl);

        MvcResult shortenResult = mockMvc.perform(post("/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, String> shortenResponseMap = objectMapper.readValue(
                shortenResult.getResponse().getContentAsString(), Map.class);
        String shortUrl = shortenResponseMap.get("shortUrl");

        // 2. 여러 번 접근
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/short-urls/{shortUrl}/redirect", shortUrl))
                    .andExpect(status().is3xxRedirection());
        }

        // 3. 통계 조회
        MvcResult statsResult = mockMvc.perform(get("/short-urls/{shortUrl}/access-stats", shortUrl))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> statsResponseMap = objectMapper.readValue(
                statsResult.getResponse().getContentAsString(), Map.class);

        // 검증
        assertThat(statsResponseMap.get("originalUrl")).isEqualTo(originalUrl);
    }
} 