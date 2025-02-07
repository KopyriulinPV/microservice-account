package com.microservice.configuration;

import com.example.RegistrationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

/*@Configuration
public class ListenerConfig {
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegistrationEvent> kafkaMessageConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, RegistrationEvent> kafkaMessageConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<String, RegistrationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaMessageConsumerFactory);
        return factory;
    }
}*/
