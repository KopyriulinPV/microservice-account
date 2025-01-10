package microservice_account.microservice_account.service;

import microservice_account.microservice_account.dto.AccountFilter;
import microservice_account.microservice_account.model.Account;

import java.util.List;
import java.util.Map;
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

}
