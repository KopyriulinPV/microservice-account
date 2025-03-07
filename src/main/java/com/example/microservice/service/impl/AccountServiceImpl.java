package com.example.microservice.service.impl;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.EventKafkaProducer;
import com.example.KafkaProperties;
import com.example.RegistrationEvent;
import com.example.microservice.dto.AccountResponseDto;
import com.example.microservice.dto.FriendShortDto;
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
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.json.JSONArray;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Value("${app.kafka.kafkaUpdateUserTopic}")
    private String topicName;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final KafkaTemplate<String, RegistrationEvent> kafkaTemplate;

    private final WebClient webClient;

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

    public Page<Account> findAccounts(String authorizationHeader, String unknownParam1, String unknownParam2, String unknownParam3,
                                      String unknownParam4, String unknownParam5, Integer size, Integer page,
                                      Boolean deleted, String ids, String firstName,
                                      String lastName, String author, String country,
                                      String city, Integer ageFrom, Integer ageTo, String statusCode, Boolean showFriends) {

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

        if (showFriends != null) {
            String baseUrl = "http://89.111.155.206:8765/api/v1/friends?statusCode=FRIEND&size=1000000";
            try {
                String ids3 = getIdsFromMsFriends(baseUrl, authorizationHeader);
                Specification<Account> spec = Specification.where(null);
                if (ids3 != null) {
                    spec = spec.and(AccountSpecifications.byNotInIds(ids3));
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
            } catch (Exception e) {
                log.error("Error while fetching account by statusCode FRIEND 6666666666666666666666666666666666666666666666666666666666666666666666666666666666: {}", e.getMessage());
                throw new RuntimeException("Ошибка при получении аккаунтов по статусу FRIEND. Пожалуйста, попробуйте позже.");
            }
        }


        if (statusCode != null && ((statusCode.equals("FRIEND")) || statusCode.equals("REQUEST_FROM") ||
                statusCode.equals("REQUEST_TO"))) {
            String baseUrl = new String();
            switch (statusCode) {
                case "FRIEND":
                    baseUrl = "http://89.111.155.206:8765/api/v1/friends?statusCode=FRIEND&size=1000000";
                    break;
                case "REQUEST_FROM":
                    baseUrl = "http://89.111.155.206:8765/api/v1/friends?statusCode=REQUEST_FROM&size=1000000";
                    break;
                case "REQUEST_TO":
                    baseUrl = "http://89.111.155.206:8765/api/v1/friends?statusCode=REQUEST_TO&size=1000000";
                    break;
            }
            System.out.println("55555555555555555555555555555555555555555555555555555555555555555555555555555");
            try {
                String ids2 = getIdsFromMsFriends(baseUrl, authorizationHeader);
                Specification<Account> spec = Specification.where(null);
                if (ids2 != null) {
                    spec = spec.and(AccountSpecifications.byIds(ids2));
                }
                if (firstName != null) {
                    spec = spec.and(AccountSpecifications.byFirstName(firstName));
                }
                try {
                    return accountRepository.findAll(spec, PageRequest.of(page - 1, size));
                } catch (DataAccessException e) {
                    log.error("Error while finding accounts: {}", e.getMessage());
                    throw new RuntimeException("Ошибка при поиске аккаунтов. Пожалуйста, попробуйте позже.");
                }
            } catch (Exception e) {
                log.error("Error while fetching account by statusCode FRIEND 6666666666666666666666666666666666666666666666666666666666666666666666666666666666: {}", e.getMessage());
                throw new RuntimeException("Ошибка при получении аккаунтов по статусу FRIEND. Пожалуйста, попробуйте позже.");
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

    public String getIdsFromMsFriends(String baseUrl, String authorizationHeader) {
        StringBuilder ids2 = new StringBuilder();
        String response = webClient.get()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + AccountService.getToken(authorizationHeader))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<String>() {
                })
                .block();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray contentArray = jsonObject.getJSONArray("content");
        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject friendObject = contentArray.getJSONObject(i);
            String friendId = friendObject.getString("friendId");
            System.out.println(friendId);
            ids2.append(friendId);
            if (i < contentArray.length() - 1) {
                ids2.append(",");
            }
        }
        System.out.println(ids2);
        return ids2.toString();
    }
}

