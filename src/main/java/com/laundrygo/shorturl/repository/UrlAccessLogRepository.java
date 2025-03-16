package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UrlAccessLogRepository {
    void save(UrlAccessLog urlAccessLog);
}
