package com.example.microservice.listener;

import com.example.RegistrationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import java.util.UUID;


/*public interface KafkaEventMessageListener {

    @KafkaListener(
            topics = "REGISTER_TOP",
            groupId = "kafka-message-group-id",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory"
    )
    default void listen(@Payload RegistrationEvent registrationEvent,
                               @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

            action(registrationEvent);
    }

    void action(RegistrationEvent registrationEvent);

}*/
