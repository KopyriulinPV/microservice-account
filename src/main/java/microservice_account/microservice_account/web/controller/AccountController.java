package microservice_account.microservice_account.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import microservice_account.microservice_account.dto.*;
import microservice_account.microservice_account.mapper.AccountMapper;
import microservice_account.microservice_account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Tag(name = "account-controller", description = "Контроллер для управления аккаунтами")
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    /*@Operation(summary = "Получение информации о текущем аккаунте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountMeDto.class))
            })
    })
    @GetMapping("/me")
    public ResponseEntity<AccountMeDto> getCurrentAccount() {
        return new ResponseEntity<>(new AccountMeDto(), HttpStatus.OK);
    }*/

    /*@Operation(summary = "Обновление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountMeDto.class))
            })
    })
    @PutMapping("/me")
    public ResponseEntity<AccountMeDto> updateAccountMe(@RequestBody AccountUpdateDto accountUpdateDto) {
        // Логика обновления аккаунта
        // ...
        return new ResponseEntity<>(new AccountMeDto(), HttpStatus.OK);
    }*/

    /*@Operation(summary = "Пометить текущий аккаунт как удалённый")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/me")
    public ResponseEntity<Void> markAccountAsDeleted() {
        // Логика пометки аккаунта как удалённого
        // ...
        return new ResponseEntity<>(HttpStatus.OK);
    }*/





    @Operation(summary = "Получение аккаунта по email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDto.class))
            })
    })
    @GetMapping
    public ResponseEntity<AccountResponseDto> getAccount(@RequestParam String email) {
        return new ResponseEntity<>(accountMapper.
                accountToAccountResponseDto(accountService.getAccountByEmail(email)), HttpStatus.OK);
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
        return new ResponseEntity<>(accountMapper.accountToAccountMeDto(
                accountService.createAccount(
                        accountMapper.accountMeDtoToAccount(accountMeDto))), HttpStatus.CREATED);
    }

    ///////!!!!!!!!!!!!!!!!! Статус код это про друзья или нет
    // /////!!!!!!!!!!!!!!!!! //// Жду frontend, с ним будет понятнее про StatusCode
    @Operation(summary = "'Прием UUID от сервиса Dialogs через Webclient о завершении сессии вебсокета у аккаунта: как флаг перехода в статус offline'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UUID успешно обработан", content = {
                    @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            }),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @PostMapping("/lastAction/{uuid}")
    public ResponseEntity<String> receiveUUIDFromPath(@PathVariable UUID id){
        accountService.markAccountAsOfflineById(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @Operation(summary = "Получение аккаунта по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDataDto.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDataDto> getAccountById(@PathVariable UUID id) {
        return new ResponseEntity<>(accountMapper.accountToAccountDataDto(accountService
                .getAccountById(id)), HttpStatus.OK);
    }

    @Operation(summary = "Пометить аккаунт как удалённый по ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> markAccountAsDeletedById(@PathVariable UUID id) {
        accountService.markAccountAsDeletedById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Пометить аккаунт как заблокированный по ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> markAccountAsBlockedById(@PathVariable UUID id) {
        accountService.markAccountAsBlockedById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Получение общего количества аккаунтов для telegram-бота")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64"))
    })
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalAccountsCount() {
        return new ResponseEntity<>(accountService.getTotalAccountsCount(), HttpStatus.OK);
    }

    @Operation(summary = "Глобальный поиск аккаунта по ключевым словам")
    @ApiResponses(value = {
            /*@ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "object", additionalProperties = @Schema(type = "object")))
            })*/
    })
    @GetMapping("/search")

    public ResponseEntity<AccountListResponse> searchAccounts(AccountFilter accountFilter) {
        return new ResponseEntity<>(accountMapper.accountListToAccountListResponse(accountService.searchAccounts(accountFilter)), HttpStatus.OK);
    }

    //// Жду frontend
   /* @Operation(summary = "Поиск аккаунта по статус-коду отношений в микросервисе Friends. Этот контроллер ссылается на глобальный поиск аккаунтов /search, так как в нем учтен statusCode.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "object", additionalProperties = @Schema(type = "object")))
            })
    })
    @GetMapping("/search/statusCode")
    public ResponseEntity<Map<String, Object>> searchByStatusCode(
            @Parameter(description = "Все параметры поиска в виде JSON объекта") @RequestParam Map<String, String> allParams
    ) {
        // Логика поиска аккаунтов по статус коду
        // ...
        return new ResponseEntity<>(accountService.searchAccounts(allParams), HttpStatus.OK);
    }*/
}

