package com.banny.motd.domain.alarm.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitterMap = new HashMap<>();

    public void save(Long userId, SseEmitter sseEmitter) {
        final String key = getKey(userId);
        emitterMap.put(key, sseEmitter);

        log.info("Set sseEmitter {}", userId);
    }

    public Optional<SseEmitter> get(Long userId) {
        final String key = getKey(userId);

        log.info("Get sseEmitter {}", userId);

        return Optional.ofNullable(emitterMap.get(key));
    }

    public void delete(Long userId) {
        emitterMap.remove(getKey(userId));
    }

    private String getKey(Long userId) {
        return "Emitter:UID" + userId;
    }

}
