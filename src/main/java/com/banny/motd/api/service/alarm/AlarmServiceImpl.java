package com.banny.motd.api.service.alarm;

import com.banny.motd.api.service.alarm.response.AlarmListServiceResponse;
import com.banny.motd.domain.alarm.Alarm;
import com.banny.motd.domain.alarm.AlarmArgs;
import com.banny.motd.domain.alarm.AlarmType;
import com.banny.motd.domain.alarm.infrastructure.AlarmRepository;
import com.banny.motd.domain.alarm.infrastructure.EmitterRepository;
import com.banny.motd.domain.user.User;
import com.banny.motd.domain.user.infrastructure.UserRepository;
import com.banny.motd.global.dto.request.SearchRequest;
import com.banny.motd.global.exception.ApiStatusType;
import com.banny.motd.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<AlarmListServiceResponse> getAlarmList(Long userId, SearchRequest request) {
        List<Alarm> alarmList = alarmRepository.getAlarmListBy(userId, request);

        return alarmList.stream()
                .map(alarm -> AlarmListServiceResponse.from(alarm, getAlarmMessage(
                        alarm.getAlarmType(),
                        alarm.getAlarmArgs(),
                        userRepository.getById(alarm.getAlarmArgs().getFromUserId()))))
                .collect(Collectors.toList());
    }

    @Override
    public SseEmitter subscribeAlarm(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId)); // 연결 종료 시 삭제
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId)); // 타임아웃 시 삭제

        try {
            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new ApplicationException(ApiStatusType.FAIL_ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }

    @Override
    public void send(AlarmType alarmType, AlarmArgs alarmArgs, Long receiverUserId) {
        User receiverUser = userRepository.getById(receiverUserId);
        User senderUser = userRepository.getById(alarmArgs.getFromUserId());

        String message = getAlarmMessage(alarmType, alarmArgs, senderUser);

        Alarm alarm = Alarm.builder()
                .userId(receiverUser.getId())
                .alarmArgs(alarmArgs)
                .alarmType(alarmType)
                .build();
        Alarm savedAlarm = alarmRepository.save(alarm);

        emitterRepository.get(receiverUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(savedAlarm.getId().toString()).name(ALARM_NAME).data(message));
            } catch (IOException e) {
                emitterRepository.delete(receiverUserId);
                throw new ApplicationException(ApiStatusType.FAIL_ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

    private String getAlarmMessage(AlarmType alarmType, AlarmArgs alarmArgs, User senderUser) {
        return alarmType.generateMessage(senderUser.getUsername());
    }

}
