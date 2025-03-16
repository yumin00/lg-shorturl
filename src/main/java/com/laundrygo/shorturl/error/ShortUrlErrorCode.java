package com.laundrygo.shorturl.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShortUrlErrorCode implements ErrorCode {
    SHORT_URL_NOT_FOUND(HttpStatus.NOT_FOUND, "requested-short-url-not-found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}