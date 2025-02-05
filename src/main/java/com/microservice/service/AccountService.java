package com.microservice.service;

import com.microservice.dto.AccountFilter;
import com.microservice.model.Account;

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

    Account update(Account account);
}
