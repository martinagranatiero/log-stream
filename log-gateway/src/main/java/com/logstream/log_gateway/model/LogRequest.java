package com.logstream.log_gateway.model;

import lombok.Data;

import java.util.Map;

@Data
public class LogRequest {
    private String serviceName;
    private String level;
    private String message;
    private Map<String, Object> metadata;
}
