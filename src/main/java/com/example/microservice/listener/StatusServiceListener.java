package com.example.microservice.listener;

/*@Service
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
}*/
