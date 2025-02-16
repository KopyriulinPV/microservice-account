package com.example.microservice.service;

import com.example.microservice.dto.AccountFilter;
import com.example.microservice.model.Account;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account createAccount(Account account);

    Account getAccountByEmail(String email);

    Account getAccountById(UUID id);

    void markAccountAsDeletedById(UUID id);

    void markAccountAsBlockedById(UUID id);

    void markAccountAsOfflineById(UUID id);

    Long getTotalAccountsCount();

    List<Account> searchAccounts(AccountFilter accountFilter);

    Page<Account> findAccount(Boolean deleted, Integer size, Integer page);

    Account update(Account account);
}
