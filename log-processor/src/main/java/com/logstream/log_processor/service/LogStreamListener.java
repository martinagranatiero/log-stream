package com.logstream.log_processor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logstream.log_processor.model.LogMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LogStreamListener {

    private static final String STREAM_KEY = "log_stream";
    private static final String CONSUMER_GROUP = "log-processor-group";
    private static final String CONSUMER_NAME = "log-processor-1";

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void start() {
        createConsumerGroupIfNotExists();

        new Thread(this::pollStream).start();
    }

    private void createConsumerGroupIfNotExists() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.latest(), CONSUMER_GROUP);
        } catch (Exception e) {
            // Group già esistente: ignorare
        }
    }

    private void pollStream() {
        StreamOperations<String, String, String> ops = redisTemplate.opsForStream();
        while (true) {
            try {
                List<MapRecord<String, String, String>> messages = ops.read(
                        Consumer.from(CONSUMER_GROUP, CONSUMER_NAME),
                        StreamReadOptions.empty().block(Duration.ofSeconds(2)).count(10),
                        StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
                );

                if (messages != null) {
                    for (MapRecord<String, String, String> msg : messages) {
                        Map<String, String> fields = msg.getValue();

                        // Estraggo la stringa JSON 'metadata' dalla mappa dei campi
                        String metadataJson = fields.get("metadata");

                        Map<String, Object> metadataMap = null;
                        if (metadataJson != null) {
                            // Deserializzo la stringa JSON in una mappa
                            metadataMap = objectMapper.readValue(metadataJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                        }

                        // Costruisco una nuova mappa con tutti i campi tranne metadata,
                        // e metto la mappa deserializzata in metadata
                        Map<String, Object> logMap = new java.util.HashMap<>(fields);
                        logMap.put("metadata", metadataMap);

                        // Converto la mappa in LogMessage
                        LogMessage log = objectMapper.convertValue(logMap, LogMessage.class);

                        System.out.println("✅ Log ricevuto: " + log);

                        // Acknowledge del messaggio
                        ops.acknowledge(STREAM_KEY, CONSUMER_GROUP, msg.getId());
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore durante la lettura dal Redis Stream: " + e.getMessage());
            }
        }
    }
}
