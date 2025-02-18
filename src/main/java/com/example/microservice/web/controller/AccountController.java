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


    @Operation(summary = "Получение информации о текущем аккаунте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountMeDto.class))
            })
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<AccountMeDto> getCurrentAccount(Authentication authentication) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер getCurrentAccount");
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UUID accountId = UUID.fromString(userDetails.getUsername());
            return new ResponseEntity<>(accountMapper.accountToAccountMeDto(
                    accountService.getAccountById(accountId)), HttpStatus.OK);
        } catch (Exception ignore) {

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(summary = "Обновление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountMeDto.class))
            })
    })
    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<AccountMeDto> updateAccountMe(Authentication authentication, @RequestBody AccountUpdateDto accountUpdateDto) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер updateAccountMe");
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UUID accountId = UUID.fromString(userDetails.getUsername());

            AccountMeDto accountMeDto = accountMapper
                    .accountToAccountMeDto(accountService.
                            update(accountMapper
                                    .AccountUpdateDtoToAccount(accountId, accountUpdateDto)));

            return new ResponseEntity<>(accountMeDto, HttpStatus.OK);
        } catch (Exception ignore) {

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @Operation(summary = "Пометить текущий аккаунт как удалённый")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> markAccountAsDeleted(Authentication authentication) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер markAccountAsDeleted");
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UUID accountId = UUID.fromString(userDetails.getUsername());
            accountService.markAccountAsDeletedById(accountId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(summary = "Получение аккаунта по email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDto.class))
            })
    })
    @GetMapping
    public ResponseEntity<AccountResponseDto> getAccount(@RequestParam String email) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер getAccount email");
        try {
            return new ResponseEntity<>(accountMapper.
                    accountToAccountResponseDto(accountService.getAccountByEmail(email)), HttpStatus.OK);
        } catch (Exception ignore) {

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


    }

    @Operation(summary = "Создание нового аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Аккаунт успешно создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountMeDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountMeDto.class))
            })
    })
    @PostMapping
    public ResponseEntity<AccountMeDto> createAccount(@RequestBody AccountMeDto accountMeDto) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер createAccount accountMeDto");
        try {
            return new ResponseEntity<>(accountMapper.accountToAccountMeDto(
                    accountService.createAccount(
                            accountMapper.accountMeDtoToAccount(accountMeDto))), HttpStatus.CREATED);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "'Прием UUID от сервиса Dialogs через Webclient о завершении сессии вебсокета у аккаунта: как флаг перехода в статус offline'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UUID успешно обработан", content = {
                    @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            }),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @PostMapping("/lastAction/{uuid}")
    public ResponseEntity<String> receiveUUIDFromPath(@PathVariable UUID id) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер receiveUUIDFromPath UUID id");
        try {
            accountService.markAccountAsOfflineById(id);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


    }


    @Operation(summary = "Получение аккаунта по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDataDto.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDataDto> getAccountById(@PathVariable UUID id) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер getAccountById UUID id");
        try {
            return new ResponseEntity<>(accountMapper.accountToAccountDataDto(accountService
                    .getAccountById(id)), HttpStatus.OK);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(summary = "Пометить аккаунт как удалённый по ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> markAccountAsDeletedById(@PathVariable UUID id) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер markAccountAsDeletedById UUID id");
        try {
            accountService.markAccountAsDeletedById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(summary = "Пометить аккаунт как заблокированный по ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> markAccountAsBlockedById(@PathVariable UUID id) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер markAccountAsBlockedById UUID id");
        try {
            accountService.markAccountAsBlockedById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Получение общего количества аккаунтов для telegram-бота")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64"))
    })
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalAccountsCount() {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер getTotalAccountsCount");
        try {
            return new ResponseEntity<>(accountService.getTotalAccountsCount(), HttpStatus.OK);
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/undefined")
    public ResponseEntity<Void> getUndefined() {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        log.info("вошел в контроллер getUndefined");
        try {
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ignore) {
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/search")
    public Page<Account> searchAccounts(
            @RequestParam(name = "size", defaultValue = "3", required = false) Integer size,
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
            @RequestParam(name = "0", required = false) String unknownParam
    ) {
        log.info("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        log.info("вошел в контроллер searchAccounts accountFilter");
        try {
            System.out.println(unknownParam);
            if (unknownParam != null) {
                String[] parts = unknownParam.split("=", 2);
                if (parts.length == 2) {
                    String paramName = parts[0];
                    String paramValue = parts[1];

                    if ("size".equals(paramName)) {
                        size = Integer.getInteger(paramValue);
                    } else if ("page".equals(paramName)) {
                        page = Integer.getInteger(paramValue);;
                    } else if ("isDeleted".equals(paramName)) {
                        isDeleted = Boolean.getBoolean(paramValue);
                    } else if ("ids".equals(paramName)) {
                        ids = paramValue;
                    } else if ("firstName".equals(paramName)) {
                        firstName = paramValue;
                    } else if ("lastName".equals(paramName)) {
                        lastName = paramValue;
                    } else if ("author".equals(paramName)) {
                        author = paramValue;
                    } else if ("country".equals(paramName)) {
                        country = paramValue;
                    } else if ("city".equals(paramName)) {
                        city = paramValue;
                    } else if ("ageFrom".equals(paramName)) {
                        ageFrom = Integer.getInteger(paramValue);
                    } else if ("ageTo".equals(paramName)) {
                        ageTo = Integer.getInteger(paramValue);
                    }
                }
            }
            return accountService.findAccounts(size, page, isDeleted, ids, firstName, lastName, author, country,
                    city, ageFrom, ageTo);
        } catch (Exception ignore) {

        }
        return null;
    }


   /*@Operation(summary = "Поиск аккаунта по статус-коду отношений в микросервисе Friends. Этот контроллер ссылается на глобальный поиск аккаунтов /search, так как в нем учтен statusCode.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "object", additionalProperties = @Schema(type = "object")))
            })
    })
    @GetMapping("/search/statusCode")
    public ResponseEntity<Map<String, Object>> searchByStatusCode(
            @Parameter(description = "Все параметры поиска в виде JSON объекта") @RequestParam Map<String, String> allParams
    ) {

        return new ResponseEntity<>(accountService.searchAccounts(allParams), HttpStatus.OK);
    }*/
}

