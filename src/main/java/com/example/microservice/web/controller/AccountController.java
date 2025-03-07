package com.example.microservice.web.controller;


import com.example.microservice.dto.*;
import com.example.microservice.mapper.AccountMapper;
import com.example.microservice.model.Account;
import com.example.microservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Tag(name = "account-controller", description = "Контроллер для управления аккаунтами")
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;


    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public AccountMeDto getCurrentAccount(Authentication authentication) {
            return accountMapper.accountToAccountMeDto(accountService.getAccountByIdForMe(
                    AccountService.getAccountId(authentication)));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public AccountMeDto updateAccountMe(Authentication authentication, @RequestBody AccountUpdateDto accountUpdateDto) {
        return accountMapper.accountToAccountMeDto(accountService.update(accountMapper
                                    .AccountUpdateDtoToAccount(
                                            AccountService.getAccountId(authentication), accountUpdateDto)));
    }


    @DeleteMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public void markAccountAsDeleted(Authentication authentication) {
            accountService.markAccountAsDeletedById(AccountService.getAccountId(authentication));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public AccountResponseDto getAccount(@RequestParam String email) {
            return accountMapper.accountToAccountResponseDto(accountService.getAccountByEmail(email));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public AccountMeDto createAccount(@RequestBody AccountMeDto accountMeDto) {
            return accountMapper.accountToAccountMeDto(accountService.createAccount(accountMapper
                    .accountMeDtoToAccount(accountMeDto)));
    }

    @PostMapping("/lastAction/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String receiveUUIDFromPath(@PathVariable UUID uuid) {
            return accountService.markLastOnlineTimeById(uuid);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<AccountDataDto> getAccountById(@PathVariable UUID id) {
            return ResponseEntity.ok(accountMapper.accountToAccountDataDto(accountService.getAccountById(id)));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public void markAccountAsDeletedById(@PathVariable UUID id) {
            accountService.markAccountAsDeletedById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public void markAccountAsBlockedById(@PathVariable UUID id) {
            accountService.markAccountAsBlockedById(id);
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Long getTotalAccountsCount() {
            return accountService.getTotalAccountsCount();
    }

    @GetMapping("/undefined")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> getUndefined() {
            return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/search")
    /*@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")*/
    public Page<Account> searchAccounts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "isDeleted", required = false) Boolean isDeleted,
            @RequestParam(name = "ids", required = false) String ids,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "ageFrom", required = false) Integer ageFrom,
            @RequestParam(name = "ageTo", required = false) Integer ageTo,
            @RequestParam(name = "0", required = false) String unknownParam1,
            @RequestParam(name = "1", required = false) String unknownParam2,
            @RequestParam(name = "2", required = false) String unknownParam3,
            @RequestParam(name = "3", required = false) String unknownParam4,
            @RequestParam(name = "4", required = false) String unknownParam5,
            @RequestParam(name = "statusCode", required = false) String statusCode
    ) {
            return accountService.findAccounts(authorizationHeader, unknownParam1, unknownParam2, unknownParam3, unknownParam4, unknownParam5, size, page, isDeleted, ids, firstName, lastName, author,
                    country, city, ageFrom, ageTo, statusCode);
    }

    @GetMapping("/search/statusCode")
    /*@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")*/
    public Page<Account> searchByStatusCode(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "isDeleted", required = false) Boolean isDeleted,
            @RequestParam(name = "ids", required = false) String ids,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "ageFrom", required = false) Integer ageFrom,
            @RequestParam(name = "ageTo", required = false) Integer ageTo,
            @RequestParam(name = "0", required = false) String unknownParam1,
            @RequestParam(name = "1", required = false) String unknownParam2,
            @RequestParam(name = "2", required = false) String unknownParam3,
            @RequestParam(name = "3", required = false) String unknownParam4,
            @RequestParam(name = "4", required = false) String unknownParam5,
            @RequestParam(name = "statusCode", required = false) String statusCode
    ) {

        return accountService.findAccounts(authorizationHeader, unknownParam1, unknownParam2, unknownParam3, unknownParam4, unknownParam5, size, page, isDeleted, ids, firstName, lastName, author,
                country, city, ageFrom, ageTo, statusCode);
    }

}

