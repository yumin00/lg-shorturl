package com.laundrygo.shorturl.service.service;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import com.laundrygo.shorturl.service.ShortUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    private final String ORIGINAL_URL = "https://www.example.com";
    private final String SHORT_URL = "Abc12345";

    @Test
    @DisplayName("기존에 없는 URL 단축 테스트")
    void shortenNewUrlTest() {
        // given
        when(urlMappingRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(null);
        when(urlMappingRepository.existsByShortUrl(anyString())).thenReturn(false);

        // when
        String shortUrl = shortUrlService.shortenUrl(ORIGINAL_URL);

        // then
        assertNotNull(shortUrl);
        assertEquals(8, shortUrl.length());
        verify(urlMappingRepository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    @DisplayName("이미 존재하는 URL 단축 테스트")
    void shortenExistingUrlTest() {
        // given
        UrlMapping existingMapping = UrlMapping.builder()
                .id(1L)
                .originalUrl(ORIGINAL_URL)
                .shortUrl(SHORT_URL)
                .build();
        
        when(urlMappingRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(existingMapping);

        // when
        String shortUrl = shortUrlService.shortenUrl(ORIGINAL_URL);

        // then
        assertEquals(SHORT_URL, shortUrl);
        verify(urlMappingRepository, never()).save(any());
    }
}