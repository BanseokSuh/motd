package com.banny.motd.domain.alarm.application.producer;

import com.banny.motd.global.helper.SerializeHelper;
import com.banny.motd.domain.alarm.domain.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<Long, AlarmEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.alarm}")
    private String topic;

    public void send(AlarmEvent event) {
        kafkaTemplate.send(topic, event.getReceiverUserId(), event);

        log.info("Send to kafka finished: {}", event);
    }

//    private final KafkaTemplate<Long, String> kafkaTemplate;
//
//    @Value("${spring.kafka.topic.alarm}")
//    private String topic;
//
//    public void send(AlarmEvent event) {
//        String message = SerializeHelper.serialize(event);
//
//        kafkaTemplate.send(topic, event.getReceiverUserId(), message);
//
//        log.info("Send to kafka finished: {}", message);
//    }
}