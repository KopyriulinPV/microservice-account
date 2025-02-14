package com.microservice.listener;

import com.example.KafkaProperties;
import com.example.RegistrationEvent;
import com.microservice.mapper.AccountMapper;
import com.microservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationUserListener {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    /*@KafkaListener(topics = "${app.kafka.kafkaRegisterTopic}",
            containerFactory = "kafkaRegistrationConcurrentKafkaListenerContainerFactory")*/



    @KafkaListener(topics = "${app.kafka.kafkaRegisterTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaRegistrationConcurrentKafkaListenerContainerFactory")
    private void listen(@Payload RegistrationEvent registrationEvent,
                        @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        log.info("Received message: {}", registrationEvent);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        registrationEvent.setDeleted(false);
        accountRepository.save(accountMapper.registrationEventToAccount(registrationEvent));
    }
}
