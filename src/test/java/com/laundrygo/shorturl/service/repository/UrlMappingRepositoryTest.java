package com.laundrygo.shorturl.service.repository;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UrlMappingRepositoryTest {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Test
    @DisplayName("원본 URL로 매핑 조회 테스트")
    void findByOriginalUrl() {
        // given
        String originalUrl = "https://www.example.com";

        // when
        UrlMapping foundMapping = urlMappingRepository.findByOriginalUrl(originalUrl);

        // then
        assertThat(foundMapping).isNotNull();
        assertThat(foundMapping.getId()).isEqualTo(1L);
        assertThat(foundMapping.getShortUrl()).isEqualTo("example");
    }
    
    @Test
    @DisplayName("존재하지 않는 원본 URL 조회 시 null 반환 테스트")
    void findByNonExistentOriginalUrl() {
        // given
        String nonExistentOriginalUrl = "https://nonexistent.com";

        // when
        UrlMapping foundMapping = urlMappingRepository.findByOriginalUrl(nonExistentOriginalUrl);

        // then
        assertThat(foundMapping).isNull();
    }

    @Test
    @DisplayName("짧은 URL 존재 여부 확인 테스트 - 존재하는 경우")
    void existsByShortUrlWhenExists() {
        // given
        String existingShortUrl = "example";

        // when
        boolean exists = urlMappingRepository.existsByShortUrl(existingShortUrl);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("짧은 URL 존재 여부 확인 테스트 - 존재하지 않는 경우")
    void existsByShortUrlWhenNotExists() {
        // given
        String nonExistentShortUrl = "nonexist";

        // when
        boolean exists = urlMappingRepository.existsByShortUrl(nonExistentShortUrl);

        // then
        assertThat(exists).isFalse();
    }
    
    @Test
    @DisplayName("새 URL 매핑 저장 후 조회 테스트")
    void saveAndFindUrlMapping() {
        // given
        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl("https://www.new-test.com")
                .shortUrl("newtest1")
                .build();

        // when
        urlMappingRepository.save(urlMapping);
        UrlMapping foundMapping = urlMappingRepository.findByOriginalUrl("https://www.new-test.com");

        // then
        assertThat(foundMapping).isNotNull();
        assertThat(foundMapping.getShortUrl()).isEqualTo("newtest1");
        assertThat(foundMapping.getCreatedAt()).isNotNull();
    }
}