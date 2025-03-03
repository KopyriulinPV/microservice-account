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

    Account getAccountByIdForMe(UUID id);

    void markAccountAsDeletedById(UUID id);

    void markAccountAsBlockedById(UUID id);

    String markLastOnlineTimeById(UUID id);

    Long getTotalAccountsCount();



    Page<Account> findAccounts(String authorizationHeader, String unknownParam1, String unknownParam2, String unknownParam3, String unknownParam4,
                               String unknownParam5, Integer size, Integer page, Boolean isDeleted, String ids,
                               String firstName, String lastName, String author, String country, String city,
                               Integer ageFrom, Integer ageTo, String statusCode);

    Account update(Account account);

    static UUID getAccountId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return UUID.fromString(userDetails.getUsername());
    }

    static String getToken(String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return token = authorizationHeader.substring(7);
        }
        return null;
    }
}
