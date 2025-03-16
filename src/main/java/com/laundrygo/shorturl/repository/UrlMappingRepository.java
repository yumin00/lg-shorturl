package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.UrlMapping;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UrlMappingRepository {
    UrlMapping findByOriginalUrl(String originalUrl);
    void save(UrlMapping urlMapping);
    boolean existsByShortUrl(String shortUrl);
}