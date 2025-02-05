package com.microservice.mapper;


import com.microservice.dto.*;
import com.microservice.model.Account;
import com.microservice.model.Role;
import events.RegistrationEvent;
import events.UpdateUserEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    Account accountMeDtoToAccount(AccountMeDto accountMeDto);


    AccountMeDto accountToAccountMeDto(Account account);

    AccountResponseDto accountToAccountResponseDto(Account account);

    AccountDataDto accountToAccountDataDto(Account account);

    Account AccountUpdateDtoToAccount(UUID id, AccountUpdateDto accountUpdateDto);

    UpdateUserEvent AccountMeDtoToUpdateUserEvent(AccountMeDto accountMeDto);

    default AccountListResponse accountListToAccountListResponse(List<Account> accountList) {
        AccountListResponse response = new AccountListResponse();
        response.setNews(accountList.stream().map(this::accountToAccountResponseDto).collect(Collectors.toList()));
        return response;
    }

    default Account registrationEventToAccount(RegistrationEvent registrationEvent) {
        Account account = new Account();
        account.setId(UUID.fromString(registrationEvent.getId()));
        account.setFirstName(registrationEvent.getFirstName());
        account.setLastName(registrationEvent.getLastName());
        account.setEmail(registrationEvent.getEmail());
        account.setPassword(registrationEvent.getPassword());
        account.setRoles(registrationEvent.getRoles().stream()
                .map(Role::fromString)
                .collect(Collectors.toSet()));
        return account;
    }
}
