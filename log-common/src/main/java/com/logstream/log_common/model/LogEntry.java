package com.logstream.log_common.model;

import com.logstream.log_common.config.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "log_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private String level;

    private String message;

    private Instant timestamp;

    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> metadata;
}
