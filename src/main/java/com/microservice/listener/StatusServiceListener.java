package com.microservice.listener;

import com.microservice.mapper.AccountMapper;
import com.microservice.repository.AccountRepository;
import events.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatusServiceListener {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @KafkaListener(topics = "${app.kafka.kafkaRegisterTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload RegistrationEvent registrationEvent,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        System.out.println(registrationEvent.toString());
        accountRepository.save(accountMapper.registrationEventToAccount(registrationEvent));

    }
}
