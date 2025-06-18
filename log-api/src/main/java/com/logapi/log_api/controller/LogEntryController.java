package com.logapi.log_api.controller;

import com.logstream.common.model.LogEntry;
import com.logapi.log_api.repository.LogEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/logs")
public class LogEntryController {

    private final LogEntryRepository repository;

    public LogEntryController(LogEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Page<LogEntry> getLogs(
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String startTimestamp,
            @RequestParam(required = false) String endTimestamp,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Instant start = null;
        Instant end = null;

        try {
            if (startTimestamp != null) start = Instant.parse(startTimestamp);
            if (endTimestamp != null) end = Instant.parse(endTimestamp);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Timestamp non valido. Usa formato ISO-8601, es: 2023-06-20T10:15:30Z");
        }

        // Logica chiamate repository
        if (serviceName != null && level != null && start != null && end != null) {
            return repository.findAllByServiceNameAndLevelAndTimestampBetween(serviceName, level, start, end, pageable);
        } else if (serviceName != null && start != null && end != null) {
            return repository.findAllByServiceNameAndTimestampBetween(serviceName, start, end, pageable);
        } else if (level != null && start != null && end != null) {
            return repository.findAllByLevelAndTimestampBetween(level, start, end, pageable);
        } else if (start != null && end != null) {
            return repository.findAllByTimestampBetween(start, end, pageable);
        } else if (serviceName != null && level != null) {
            return repository.findAllByServiceNameAndLevel(serviceName, level, pageable);
        } else if (serviceName != null) {
            return repository.findAllByServiceName(serviceName, pageable);
        } else if (level != null) {
            return repository.findAllByLevel(level, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
}
