package com.banny.motd.domain.event;

import com.banny.motd.global.exception.ApiStatusType;
import com.banny.motd.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public enum EventType {

    REGULAR("정기모임"),
    SPECIAL("특별모임");

    private final String text;

    public static EventType from(String eventTypeStr) {
        for (EventType eventType : EventType.values()) {
            if (eventType.name().equalsIgnoreCase(eventTypeStr)) {
                return eventType;
            }
        }

        throw new ApplicationException(ApiStatusType.FAIL_INVALID_PARAMETER, "Invalid eventType");
    }

}
