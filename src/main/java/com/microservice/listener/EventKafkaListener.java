package com.microservice.listener;

import com.example.RegistrationEvent;
import com.microservice.mapper.AccountMapper;
import com.microservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



/*@Component
@RequiredArgsConstructor
public class EventKafkaListener implements KafkaEventMessageListener {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Override
    public void action(RegistrationEvent registrationEvent) {
        accountRepository.save(accountMapper.registrationEventToAccount(registrationEvent));
    }
}*/
