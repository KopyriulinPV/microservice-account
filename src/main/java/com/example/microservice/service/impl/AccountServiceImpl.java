package com.example.microservice.service.impl;

import com.example.EventKafkaProducer;
import com.example.KafkaProperties;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Value("${app.kafka.kafkaUpdateUserTopic}")
    private String topicName;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final KafkaTemplate<String, RegistrationEvent> kafkaTemplate;

    @Override
    public Account createAccount(Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataAccessException e) {
            log.error("Error while saving the account: {}", e.getMessage());
            throw new RuntimeException("Ошибка при создании аккаунта. Пожалуйста, попробуйте позже.");
        }
    }

    @Override
    public Account getAccountByEmail(String email) {
        try {
            return accountRepository.findByEmail(email).orElseThrow(() ->
                    new NoSuchElementException("Аккаунт с email " + email + " не найден."));
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while fetching account by email: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении аккаунта. Пожалуйста, попробуйте позже.");
        }
    }

    @Override
    public Account getAccountById(UUID id) {
        try {
            return accountRepository.findById(id).orElseThrow(() ->
                    new NoSuchElementException("Аккаунт с ID " + id + " не найден."));
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while fetching account by ID: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении аккаунта. Пожалуйста, попробуйте позже.");
        }
    }

    public Account getAccountByIdForMe(UUID id) {
        try {
            Account account = accountRepository.findById(id).orElseThrow(() ->
                    new NoSuchElementException("Аккаунт с ID " + id + " не найден."));
            account.setIsOnline(true);
            return accountRepository.save(account);
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while updating account online status: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении своего аккаунта. Пожалуйста, попробуйте позже.");
        }
    }

    @Override
    public void markAccountAsDeletedById(UUID id) {
        try {
            Account account = accountRepository.findById(id).orElseThrow(() ->
                    new NoSuchElementException("Аккаунт с ID " + id + " не найден."));
            account.setDeleted(true);
            account.setDeletionTimestamp(ZonedDateTime.now());
            kafkaTemplate.send(topicName, accountMapper.accountToRegistrationEvent(account));
            accountRepository.save(account);
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while marking account as deleted: {}", e.getMessage());
            throw new RuntimeException("Ошибка при пометке аккаунта как удаленного. Пожалуйста, попробуйте позже.");
        }
    }

    @Override
    public void markAccountAsBlockedById(UUID id) {
        try {
            Account account = accountRepository.findById(id).orElseThrow(() ->
                    new NoSuchElementException("Аккаунт с ID " + id + " не найден."));
            account.setBlocked(true);
            accountRepository.save(account);
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while marking account as blocked: {}", e.getMessage());
            throw new RuntimeException("Ошибка при блокировке аккаунта. Пожалуйста, попробуйте позже.");
        }
    }

    @Override
    public String markLastOnlineTimeById(UUID id) {
        try {
            Account account = accountRepository.findById(id).orElseThrow(() ->
                    new NoSuchElementException("Аккаунт с ID " + id + " не найден."));
            account.setLastOnlineTime(ZonedDateTime.now());
            account.setIsOnline(false);
            accountRepository.save(account);
            return "UUID успешно обработан";
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while marking last online time: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении времени последнего входа. Пожалуйста, попробуйте позже.");
        }
    }

    @Override
    public Long getTotalAccountsCount() {
        try {
        return accountRepository.count();
        } catch (DataAccessException e) {
            log.error("Error while counting accounts: {}", e.getMessage());
            throw new RuntimeException("Ошибка при подсчете аккаунтов. Пожалуйста, попробуйте позже.");
        }
    }

    public Account update(Account account) {
        try {
        Account existedAccount = accountRepository.findById(account.getId()).orElseThrow(() ->
                new NoSuchElementException("Аккаунт с ID " + account.getId() + " не найден."));
        BeanUtils.copyNonNullProperties(account, existedAccount);

        kafkaTemplate.send(topicName, accountMapper.accountToRegistrationEvent(existedAccount));
        existedAccount.setUpdatedOn(ZonedDateTime.now());
        return accountRepository.save(existedAccount);
        } catch (NoSuchElementException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error while updating account: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении аккаунта. Пожалуйста, попробуйте позже.");
        }
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

        if (author != null) {
            if (author != null) {
                Specification<Account> spec = Specification.where(null);
                String[] params = author.split(" ");
                for (String param : params) {
                    spec = spec.and(AccountSpecifications.byFirstName(param))
                            .or(AccountSpecifications.byLastName(param));
                }
                if (deleted != null) {
                    spec = spec.and(AccountSpecifications.byIsDeleted(deleted));
                }
                try {
                    return accountRepository.findAll(spec, PageRequest.of(page - 1, size));
                } catch (DataAccessException e) {
                    log.error("Error while finding accounts: {}", e.getMessage());
                    throw new RuntimeException("Ошибка при поиске аккаунтов. Пожалуйста, попробуйте позже.");
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
        if (country != null) {
            spec = spec.and(AccountSpecifications.byCountry(country));
        }
        if (city != null) {
            spec = spec.and(AccountSpecifications.byCity(city));
        }
        if (ageFrom != null || ageTo != null) {
            spec = spec.and(AccountSpecifications.byAgeRange(ageFrom, ageTo));
        }
        try {
        return accountRepository.findAll(spec, PageRequest.of(page - 1, size));
        } catch (DataAccessException e) {
            log.error("Error while finding accounts: {}", e.getMessage());
            throw new RuntimeException("Ошибка при поиске аккаунтов. Пожалуйста, попробуйте позже.");
        }
    }


}
