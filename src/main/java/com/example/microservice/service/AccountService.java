package com.example.microservice.service;

import com.example.microservice.dto.AccountFilter;
import com.example.microservice.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account createAccount(Account account);

    Account getAccountByEmail(String email);

    Account getAccountById(UUID id);

    void markAccountAsDeletedById(UUID id);

    void markAccountAsBlockedById(UUID id);

    String markAccountAsOfflineById(UUID id);

    Long getTotalAccountsCount();

    List<Account> searchAccounts(AccountFilter accountFilter);

    Page<Account> findAccounts(String unknownParam, Integer size, Integer page, Boolean isDeleted, String ids, String firstName,
                               String lastName, String author, String country, String city, Integer ageFrom, Integer ageTo);

    Account update(Account account);

    static UUID getAccountId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return UUID.fromString(userDetails.getUsername());
    }
}
