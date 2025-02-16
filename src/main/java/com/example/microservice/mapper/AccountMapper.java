package com.example.microservice.mapper;


import com.example.RegistrationEvent;
import com.example.microservice.dto.*;
import com.example.microservice.model.Account;
import com.example.microservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface AccountMapper {

    default Account accountMeDtoToAccount(AccountMeDto accountMeDto) {
        Account account = new Account();
        account.setId(UUID.fromString(accountMeDto.getId()));
        account.setFirstName(accountMeDto.getFirstName());
        account.setLastName(accountMeDto.getLastName());
        account.setEmail(accountMeDto.getEmail());
        account.setPassword(accountMeDto.getPassword());
        account.setPhone(accountMeDto.getPhone());
        account.setPhoto(accountMeDto.getPhoto());
        account.setProfileCover(accountMeDto.getProfileCover());
        account.setAbout(accountMeDto.getAbout());
        account.setCity(accountMeDto.getCity());
        account.setCountry(accountMeDto.getCountry());
        account.setStatusCode(accountMeDto.getStatusCode());

        if (account.getRegDate() != null) {
            account.setRegDate(LocalDateTime.parse(accountMeDto.getRegDate()));
        }

        if (account.getBirthDate() != null) {
            account.setBirthDate(OffsetDateTime.parse(accountMeDto.getBirthDate()));
        }

        account.setMessagePermission(accountMeDto.getMessagePermission());

        if (account.getLastOnlineTime() != null) {
            account.setLastOnlineTime(LocalDateTime.parse(accountMeDto.getLastOnlineTime()));
        }

        account.setEmojiStatus(accountMeDto.getEmojiStatus());

        if (account.getCreatedOn() != null) {
            account.setCreatedOn(LocalDateTime.parse(accountMeDto.getCreatedOn()));
        }

        if (account.getUpdatedOn() != null) {
            account.setUpdatedOn(LocalDateTime.parse(accountMeDto.getUpdatedOn()));
        }

        if (account.getDeletionTimestamp() != null) {
            account.setDeletionTimestamp(LocalDateTime.parse(accountMeDto.getDeletionTimestamp()));
        }

        account.setDeleted(account.getDeleted());
        account.setBlocked(account.getBlocked());
        account.setIsOnline(account.getIsOnline());



        return account;
    }


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

        if (account.getRegDate() != null) {
            accountMeDto.setRegDate(account.getRegDate().toString());
        }

        if (account.getBirthDate() != null) {
            accountMeDto.setBirthDate(account.getBirthDate().toString());
        }

        accountMeDto.setMessagePermission(account.getMessagePermission());

        if (account.getLastOnlineTime() != null) {
            accountMeDto.setLastOnlineTime(account.getLastOnlineTime().toString());
        }

        accountMeDto.setEmojiStatus(account.getEmojiStatus());

        if (account.getCreatedOn() != null) {
            accountMeDto.setCreatedOn(account.getCreatedOn().toString());
        }

        if (account.getUpdatedOn() != null) {
            accountMeDto.setUpdatedOn(account.getUpdatedOn().toString());
        }

        if (account.getDeletionTimestamp() != null) {
            accountMeDto.setDeletionTimestamp(account.getDeletionTimestamp().toString());
        }

        accountMeDto.setDeleted(account.getDeleted());
        accountMeDto.setBlocked(account.getBlocked());
        accountMeDto.setIsOnline(account.getIsOnline());

        accountMeDto.setRoles(account.getRoles().stream()
                .map(Role::toString)
                .collect(Collectors.toSet()));

        return accountMeDto;
    }


    default AccountResponseDto accountToAccountResponseDto(Account account){
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId().toString());
        accountResponseDto.setFirstName(account.getFirstName());
        accountResponseDto.setLastName(account.getLastName());
        accountResponseDto.setEmail(account.getEmail());
        accountResponseDto.setPassword(account.getPassword());
        accountResponseDto.setRoles(account.getRoles().stream()
                .map(Role::toString)
                .collect(Collectors.toSet()));

        accountResponseDto.setPhone(account.getPhone());
        accountResponseDto.setPhoto(account.getPhoto());
        accountResponseDto.setProfileCover(account.getProfileCover());
        accountResponseDto.setAbout(account.getAbout());
        accountResponseDto.setCity(account.getCity());
        accountResponseDto.setCountry(account.getCountry());
        accountResponseDto.setStatusCode(account.getStatusCode());

        if (account.getRegDate() != null) {
            accountResponseDto.setRegDate(account.getRegDate().toString());
        }

        if (account.getBirthDate() != null) {
            accountResponseDto.setBirthDate(account.getBirthDate().toString());
        }

        accountResponseDto.setMessagePermission(account.getMessagePermission());

        if (account.getLastOnlineTime() != null) {
            accountResponseDto.setLastOnlineTime(account.getLastOnlineTime().toString());
        }

        accountResponseDto.setEmojiStatus(account.getEmojiStatus());

        if (account.getCreatedOn() != null) {
            accountResponseDto.setCreatedOn(account.getCreatedOn().toString());
        }

        if (account.getUpdatedOn() != null) {
            accountResponseDto.setUpdatedOn(account.getUpdatedOn().toString());
        }

        if (account.getDeletionTimestamp() != null) {
            accountResponseDto.setDeletionTimestamp(account.getDeletionTimestamp().toString());
        }

        accountResponseDto.setDeleted(account.getDeleted());
        accountResponseDto.setBlocked(account.getBlocked());
        accountResponseDto.setIsOnline(account.getIsOnline());

        return accountResponseDto;
    }


    default AccountDataDto accountToAccountDataDto(Account account){
        AccountDataDto accountDataDto = new AccountDataDto();
        accountDataDto.setId(account.getId().toString());
        accountDataDto.setFirstName(account.getFirstName());
        accountDataDto.setLastName(account.getLastName());
        accountDataDto.setPhone(account.getPhone());
        accountDataDto.setPhoto(account.getPhoto());
        accountDataDto.setProfileCover(account.getProfileCover());
        accountDataDto.setAbout(account.getAbout());
        accountDataDto.setCity(account.getCity());
        accountDataDto.setCountry(account.getCountry());
        accountDataDto.setStatusCode(account.getStatusCode());

        if (account.getBirthDate() != null) {
            accountDataDto.setBirthDate(account.getBirthDate().toString());
        }

        accountDataDto.setMessagePermission(account.getMessagePermission());

        if (account.getLastOnlineTime() != null) {
            accountDataDto.setLastOnlineTime(account.getLastOnlineTime().toString());
        }
        accountDataDto.setEmojiStatus(account.getEmojiStatus());
        accountDataDto.setDeleted(account.getDeleted());
        accountDataDto.setBlocked(account.getBlocked());
        accountDataDto.setIsOnline(account.getIsOnline());

        return accountDataDto;
    }


    default Account AccountUpdateDtoToAccount(UUID id, AccountUpdateDto accountUpdateDto){
        Account account = new Account();
        account.setId(id);
        account.setFirstName(accountUpdateDto.getFirstName());
        account.setLastName(accountUpdateDto.getLastName());

        if (accountUpdateDto.getBirthDate() != null && !accountUpdateDto.getBirthDate().equals("none")) {
            account.setBirthDate(OffsetDateTime.parse(accountUpdateDto.getBirthDate()));
        }

        account.setPhone(accountUpdateDto.getPhone());
        account.setAbout(accountUpdateDto.getAbout());
        account.setCity(accountUpdateDto.getCity());
        account.setCountry(accountUpdateDto.getCountry());
        if (accountUpdateDto.getBirthDate() != null) {
            account.setEmojiStatus(accountUpdateDto.getEmojiStatus());
        }

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
        account.setDeleted(registrationEvent.getDeleted());
        account.setRoles(registrationEvent.getRoles().stream()
                .map(Role::fromString)
                .collect(Collectors.toSet()));
        return account;
    }
    default RegistrationEvent accountToRegistrationEvent(Account account) {
        RegistrationEvent registrationEvent = new RegistrationEvent();

        registrationEvent.setId(account.getId().toString());
        registrationEvent.setFirstName(account.getFirstName());
        registrationEvent.setLastName(account.getLastName());
        registrationEvent.setEmail(account.getEmail());
        registrationEvent.setPassword(account.getPassword());
        registrationEvent.setDeleted(account.getDeleted());
        registrationEvent.setRoles(account.getRoles().stream()
                .map(Role::toString)
                .collect(Collectors.toSet()));
        return registrationEvent;
    }
}
