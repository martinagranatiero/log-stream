package com.logstream.log_processor.model;

import lombok.Data;

import java.util.Map;

@Data
public class LogMessage {
    private String serviceName;
    private String level;
    private String message;
    private Map<String, Object> metadata;

    @Override
    public String toString() {
        return "[" + level + "] " + serviceName + ": " + message + " (metadata: " + metadata + ")";
    }
}
