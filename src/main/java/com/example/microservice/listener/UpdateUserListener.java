package com.example.microservice.listener;

import com.example.RegistrationEvent;
import com.example.microservice.mapper.AccountMapper;
import com.example.microservice.model.Account;
import com.example.microservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateUserListener {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;


    @KafkaListener(topics = "${app.kafka.kafkaUpdateUserTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId2}",
            containerFactory = "kafkaRegistrationConcurrentKafkaListenerContainerFactory")
    private void listen(@Payload RegistrationEvent registrationEvent,
                        @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        System.out.println("9999999999999999999999999999999999999999999999999999999999999999999" + registrationEvent.getEmail());
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        Account account = accountRepository.findById(UUID.fromString(registrationEvent.getId())).get();
        account.setPassword(registrationEvent.getPassword());
        accountRepository.save(account);
    }
}
