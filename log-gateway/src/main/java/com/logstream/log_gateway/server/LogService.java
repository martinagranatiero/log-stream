package com.logstream.log_gateway.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logstream.log_gateway.model.LogRequest;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public LogService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishLog(LogRequest log) {
        Map<String, Object> map = objectMapper.convertValue(log, Map.class);
        redisTemplate.opsForStream().add(MapRecord.create("log_stream", map));
    }
}
