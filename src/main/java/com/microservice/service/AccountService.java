package com.microservice.service;

import com.microservice.dto.AccountFilter;
import com.microservice.model.Account;
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

    Page<Account> findAccount(Boolean isDeleted, Integer size);

    Account update(Account account);
}
