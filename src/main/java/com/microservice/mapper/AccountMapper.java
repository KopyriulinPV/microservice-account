package com.microservice.mapper;

import com.example.RegistrationEvent;
import com.microservice.dto.*;
import com.microservice.model.Account;
import com.microservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    Account accountMeDtoToAccount(AccountMeDto accountMeDto);


    default AccountMeDto accountToAccountMeDto(Account account) {
        AccountMeDto accountMeDto = new AccountMeDto();
        accountMeDto.setId(account.getId().toString());
        accountMeDto.setFirstName(account.getFirstName());
        accountMeDto.setLastName(account.getLastName());
        accountMeDto.setEmail(account.getEmail());
        accountMeDto.setPassword(account.getPassword());
        accountMeDto.setPhone(account.getPhone());
        accountMeDto.setPhoto(account.getPhoto());
        accountMeDto.setProfileCover(account.getProfileCover());
        accountMeDto.setAbout(account.getAbout());
        accountMeDto.setCity(account.getCity());
        accountMeDto.setCountry(account.getCountry());
        accountMeDto.setStatusCode(account.getStatusCode());

        String regDateString = (account.getRegDate() != null) ? account.getRegDate().toString()
                : " ";
        accountMeDto.setRegDate(regDateString);

        String birthDateString = (account.getBirthDate() != null) ? account.getBirthDate().toString()
                : " ";
        accountMeDto.setBirthDate(birthDateString);


        accountMeDto.setMessagePermission(account.getMessagePermission());

        String lastOnlineTimeString = (account.getLastOnlineTime() != null) ? account.getLastOnlineTime().toString()
                : " ";
        accountMeDto.setLastOnlineTime(lastOnlineTimeString);

        accountMeDto.setEmojiStatus(account.getEmojiStatus());

        String createdOn = (account.getCreatedOn() != null) ? account.getCreatedOn().toString()
                : " ";
        accountMeDto.setCreatedOn(createdOn);

        String updatedOn = (account.getUpdatedOn() != null) ? account.getUpdatedOn().toString()
                : " ";
        accountMeDto.setUpdatedOn(updatedOn);

        String deletionTimestamp = (account.getDeletionTimestamp() != null) ? account.getDeletionTimestamp().toString()
                : " ";
        accountMeDto.setDeletionTimestamp(deletionTimestamp);

        accountMeDto.setDeleted(account.getDeleted());
        accountMeDto.setBlocked(account.getBlocked());
        accountMeDto.setIsOnline(account.getIsOnline());

        return accountMeDto;
    }



    AccountResponseDto accountToAccountResponseDto(Account account);

    AccountDataDto accountToAccountDataDto(Account account);

    default Account AccountUpdateDtoToAccount(UUID id, AccountUpdateDto accountUpdateDto){
        Account account = new Account();
        account.setId(id);
        account.setFirstName(accountUpdateDto.getFirstName());
        account.setLastName(accountUpdateDto.getLastName());

        if (accountUpdateDto.getBirthDate() != null) {
            account.setBirthDate(LocalDateTime.parse(accountUpdateDto.getBirthDate()));
        }

        account.setPhone(accountUpdateDto.getPhone());
        account.setAbout(accountUpdateDto.getAbout());
        account.setCity(accountUpdateDto.getCity());
        account.setCountry(accountUpdateDto.getCountry());
        account.setEmojiStatus(accountUpdateDto.getEmojiStatus());
        account.setPhoto(accountUpdateDto.getPhoto());
        account.setProfileCover(accountUpdateDto.getProfileCover());
        return account;
    }


    RegistrationEvent AccountMeDtoToRegistrationEvent(AccountMeDto accountMeDto);

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
