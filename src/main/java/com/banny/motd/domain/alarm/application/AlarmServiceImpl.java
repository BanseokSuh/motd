package com.banny.motd.domain.alarm.application;

import com.banny.motd.domain.alarm.application.repository.AlarmRepository;
import com.banny.motd.domain.alarm.application.repository.EmitterRepository;
import com.banny.motd.domain.alarm.domain.Alarm;
import com.banny.motd.domain.alarm.domain.AlarmArgs;
import com.banny.motd.domain.alarm.domain.AlarmType;
import com.banny.motd.domain.alarm.infrastructure.entity.AlarmEntity;
import com.banny.motd.domain.user.application.repository.UserRepository;
import com.banny.motd.domain.user.domain.User;
import com.banny.motd.domain.user.infrastructure.entity.UserEntity;
import com.banny.motd.global.exception.ApplicationException;
import com.banny.motd.global.exception.ResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "motd-alarm";
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Alarm> getAlarmList(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public SseEmitter subscribeAlarm(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId)); // 연결 종료 시 삭제
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId)); // 타임아웃 시 삭제

        try {
            // 연결 완료 메시지 전송
            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new ApplicationException(ResultType.FAIL_ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }

    @Override
    public void send(AlarmType alarmType, AlarmArgs alarmArgs, Long receiverUserId) {
        User receiverUser = getUserById(receiverUserId);
        User senderUser = getUserById(alarmArgs.getFromUserId());

        String message = getAlarmMessage(alarmType, alarmArgs, senderUser);

        AlarmEntity alarmEntity = alarmRepository.save(AlarmEntity.of(receiverUser.getId(), alarmType, alarmArgs));

        emitterRepository.get(receiverUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmEntity.getId().toString()).name(ALARM_NAME).data(message));
            } catch (IOException e) {
                emitterRepository.delete(receiverUserId);
                throw new ApplicationException(ResultType.FAIL_ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserEntity::toDomain)
                .orElseThrow(() -> new ApplicationException(ResultType.FAIL_USER_NOT_FOUND, String.format("User not found: %d", userId)));
    }

    private String getAlarmMessage(AlarmType alarmType, AlarmArgs alarmArgs, User senderUser) {
        StringBuilder message = new StringBuilder();

        if (alarmType == AlarmType.COMMENT) {
            message.append(String.format("%s from %s", alarmType.getAlarmText(), senderUser.getUsername()));
        } else if (alarmType == AlarmType.LIKE) {
            message.append(String.format("%s from %s", alarmType.getAlarmText(), senderUser.getUsername()));
        } else {
            message.append(String.format("New alarm %s", senderUser.getUsername()));
        }

        return message.toString();
    }
}
