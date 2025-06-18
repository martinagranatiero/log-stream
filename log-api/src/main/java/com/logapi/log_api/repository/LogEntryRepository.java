package com.logapi.log_api.repository;

import com.logstream.common.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    Page<LogEntry> findAllByServiceName(String serviceName, Pageable pageable);

    Page<LogEntry> findAllByLevel(String level, Pageable pageable);

    Page<LogEntry> findAllByServiceNameAndLevel(String serviceName, String level, Pageable pageable);

    Page<LogEntry> findAllByTimestampBetween(Instant start, Instant end, Pageable pageable);

    Page<LogEntry> findAllByServiceNameAndTimestampBetween(String serviceName, Instant start, Instant end, Pageable pageable);

    Page<LogEntry> findAllByLevelAndTimestampBetween(String level, Instant start, Instant end, Pageable pageable);

    Page<LogEntry> findAllByServiceNameAndLevelAndTimestampBetween(String serviceName, String level, Instant start, Instant end, Pageable pageable);

}
