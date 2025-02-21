package com.example.microservice.service.impl;

import com.example.EventKafkaProducer;
import com.example.RegistrationEvent;
import com.example.microservice.dto.AccountResponseDto;
import com.example.microservice.service.AccountService;
import com.example.microservice.mapper.AccountMapper;
import com.example.microservice.repository.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import com.example.microservice.dto.AccountFilter;
import com.example.microservice.model.Account;
import com.example.microservice.repository.AccountRepository;
import com.example.microservice.repository.AccountSpecification;
import com.example.microservice.utils.BeanUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
/*@Import({EventKafkaProducer.class, KafkaProperties.class, EventKafkaProducerConfig.class})*/
public class AccountServiceImpl implements AccountService {

    @Value("${app.kafka.kafkaUpdateUserTopic}")
    private String topicName;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final KafkaTemplate<String, RegistrationEvent> kafkaTemplate;


    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).get();
    }

    @Override
    public Account getAccountById(UUID id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public void markAccountAsDeletedById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setDeleted(true);
        account.setDeletionTimestamp(ZonedDateTime.now());
        kafkaTemplate.send(topicName, accountMapper.accountToRegistrationEvent(account));
        /*eventKafkaProducer.sendMessageUpdateUser();*/
        accountRepository.save(account);
    }

    @Override
    public void markAccountAsBlockedById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setBlocked(true);
        accountRepository.save(account);
    }

    @Override
    public String markLastOnlineTimeById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setLastOnlineTime(ZonedDateTime.now());
        accountRepository.save(account);
        return "UUID успешно обработан";
    }

    @Override
    public Long getTotalAccountsCount() {
        return accountRepository.count();
    }

    public Account update(Account account) {
        Account existedAccount = accountRepository.findById(account.getId()).get();
        BeanUtils.copyNonNullProperties(account, existedAccount);

        /*eventKafkaProducer.sendMessageUpdateUser(accountMapper.accountToRegistrationEvent(existedAccount));*/
        existedAccount.setUpdatedOn(ZonedDateTime.now());
        return accountRepository.save(existedAccount);
    }

    @Override
    public List<Account> searchAccounts(AccountFilter accountFilter) {
        return accountRepository.findAll(AccountSpecification.withFilter(accountFilter),
                PageRequest.of(
                        accountFilter.getPage(), accountFilter.getSize()
                )).getContent();
    }

    public Page<Account> findAccounts(String unknownParam1, String unknownParam2, String unknownParam3,
                                      String unknownParam4, String unknownParam5, Integer size, Integer page,
                                      Boolean deleted, String ids, String firstName,
                                      String lastName, String author, String country,
                                      String city, Integer ageFrom, Integer ageTo) {

        String[] unknownParams = {unknownParam1, unknownParam2, unknownParam3, unknownParam4, unknownParam5};

        for (String unknownParam : unknownParams) {
            if (unknownParam != null) {
                String[] parts = unknownParam.split("=", 2);
                if (parts.length == 2) {
                    String paramName = parts[0];
                    System.out.println(paramName);
                    String paramValue = parts[1];
                    System.out.println(paramValue);
                    switch (paramName) {
                        case "size":
                            size = Integer.valueOf(paramValue);
                            break;
                        case "page":
                            page = Integer.valueOf(paramValue);
                            break;
                        case "isDeleted":
                            deleted = Boolean.valueOf(paramValue);
                            break;
                        case "ids":
                            ids = paramValue;
                            break;
                        case "firstName":
                            firstName = paramValue;
                            break;
                        case "lastName":
                            lastName = paramValue;
                            break;
                        case "author":
                            author = paramValue;
                            break;
                        case "country":
                            country = paramValue;
                            break;
                        case "city":
                            city = paramValue;
                            break;
                        case "ageFrom":
                            ageFrom = Integer.valueOf(paramValue);
                            break;
                        case "ageTo":
                            ageTo = Integer.valueOf(paramValue);
                            break;
                    }
                }
            }
        }

        Specification<Account> spec = Specification.where(null);
        if (deleted != null) {
            spec = spec.and(AccountSpecifications.byIsDeleted(deleted));
        }
        if (ids != null) {
            spec = spec.and(AccountSpecifications.byIds(ids));
        }
        if (firstName != null) {
            spec = spec.and(AccountSpecifications.byFirstName(firstName));
        }
        if (lastName != null) {
            spec = spec.and(AccountSpecifications.byLastName(lastName));
        }
        /*author*/
        if (country != null) {
            spec = spec.and(AccountSpecifications.byCountry(country));
        }
        if (city != null) {
            spec = spec.and(AccountSpecifications.byCity(city));
        }
        if (ageFrom != null || ageTo != null) {
            spec = spec.and(AccountSpecifications.byAgeRange(ageFrom, ageTo));
        }

        return accountRepository.findAll(spec, PageRequest.of(page - 1, size));
    }



}
