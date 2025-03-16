package com.laundrygo.shorturl.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundrygo.shorturl.controller.ShortUrlController;
import com.laundrygo.shorturl.dto.request.ShortenUrlRequest;
import com.laundrygo.shorturl.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShortUrlService shortUrlService;

    private final String ORIGINAL_URL = "https://www.example.com";
    private final String SHORT_URL = "Abc12345";

    @BeforeEach
    void setUp() {
        when(shortUrlService.shortenUrl(ORIGINAL_URL)).thenReturn(SHORT_URL);
    }

    @Test
    @DisplayName("URL 단축 API 테스트")
    void shortenUrlTest() throws Exception {
        // given
        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl(ORIGINAL_URL);

        // when, then
        mockMvc.perform(post("/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(SHORT_URL));
    }

    @Test
    @DisplayName("빈 URL 요청시 예외 발생 테스트")
    void shortenUrlEmptyUrlTest() throws Exception {
        // given
        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl("");

        // when, then
        mockMvc.perform(post("/short-urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("단축 URL로 리다이렉트 테스트")
    void redirectToOriginalUrlTest() throws Exception {
        // given
        when(shortUrlService.getOriginalUrlAndCreateAccessLog(SHORT_URL)).thenReturn(ORIGINAL_URL);

        // when, then
        mockMvc.perform(get("/short-urls/{shortUrl}/redirect", SHORT_URL))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", ORIGINAL_URL));
    }

    @Test
    @DisplayName("존재하지 않는 단축 URL 리다이렉트 테스트")
    void redirectToOriginalUrlNotFoundTest() throws Exception {
        // given
        when(shortUrlService.getOriginalUrlAndCreateAccessLog(anyString())).thenReturn(null);

        // when, then
        mockMvc.perform(get("/short-urls/{shortUrl}/redirect", "notExist"))
                .andExpect(status().isNotFound());
    }

}