package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.dto.request.ShortenUrlRequest;
import com.laundrygo.shorturl.dto.response.ShortenUrlResponse;
import com.laundrygo.shorturl.error.CommonErrorCode;
import com.laundrygo.shorturl.error.RestApiException;
import com.laundrygo.shorturl.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/short-urls")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request) {
        String originalUrl = request.getUrl();
        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new RestApiException(CommonErrorCode.INVALID_ARGUMENT);
        }

        String shortUrl = shortUrlService.shortenUrl(originalUrl);

        ShortenUrlResponse response = ShortenUrlResponse.builder().shortUrl(shortUrl).build();

        return ResponseEntity.ok(response);
    }
}
