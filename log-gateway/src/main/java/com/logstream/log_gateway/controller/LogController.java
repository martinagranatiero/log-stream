package com.logstream.gateway.controller;

import com.logstream.gateway.dto.LogRequest;
import com.logstream.gateway.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<String> receiveLog(@RequestBody LogRequest log) {
        logService.publishLog(log);
        return ResponseEntity.ok("Log ricevuto e inviato a Redis");
    }
}
