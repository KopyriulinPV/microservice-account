package microservice_account.microservice_account.mapper;

import microservice_account.microservice_account.dto.AccountDataDto;
import microservice_account.microservice_account.dto.AccountListResponse;
import microservice_account.microservice_account.dto.AccountMeDto;
import microservice_account.microservice_account.dto.AccountResponseDto;
import microservice_account.microservice_account.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    Account accountMeDtoToAccount(AccountMeDto accountMeDto);

    AccountMeDto accountToAccountMeDto(Account account);

    AccountResponseDto accountToAccountResponseDto(Account account);

    AccountDataDto accountToAccountDataDto(Account account);

    default AccountListResponse accountListToAccountListResponse(List<Account> accountList) {
        AccountListResponse response = new AccountListResponse();
        response.setNews(accountList.stream().map(this::accountToAccountResponseDto).collect(Collectors.toList()));
        return response;
    }
}
