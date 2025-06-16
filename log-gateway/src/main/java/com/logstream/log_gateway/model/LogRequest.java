package com.logstream.gateway.dto;

import java.util.Map;

public class LogRequest {
    private String serviceName;
    private String level;
    private String message;
    private Map<String, Object> metadata;

    public LogRequest(String serviceName, String level, String message, Map<String, Object> metadata) {
        this.serviceName = serviceName;
        this.level = level;
        this.message = message;
        this.metadata = metadata;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
