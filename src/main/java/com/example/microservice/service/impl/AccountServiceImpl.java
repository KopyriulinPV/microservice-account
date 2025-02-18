package com.example.microservice.service.impl;

import com.example.EventKafkaProducer;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
/*@Import({EventKafkaProducer.class, KafkaProperties.class, EventKafkaProducerConfig.class})*/
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    /*private final EventKafkaProducer eventKafkaProducer;*/


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
        /*eventKafkaProducer.sendMessageUpdateUser(accountMapper.accountToRegistrationEvent(account));*/
        accountRepository.save(account);
    }

    @Override
    public void markAccountAsBlockedById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setBlocked(true);
        accountRepository.save(account);
    }

    @Override
    public String markAccountAsOfflineById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setStatusCode("Offline");
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

        return accountRepository.save(existedAccount);
    }

    @Override
    public List<Account> searchAccounts(AccountFilter accountFilter) {
        return accountRepository.findAll(AccountSpecification.withFilter(accountFilter),
                PageRequest.of(
                        accountFilter.getPage(), accountFilter.getSize()
                )).getContent();
    }

    public Page<Account> findAccounts(Integer size, Integer page, Boolean deleted, String ids, String firstName,
                                      String lastName, String author, String country,
                                      String city, Integer ageFrom, Integer ageTo) {
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
