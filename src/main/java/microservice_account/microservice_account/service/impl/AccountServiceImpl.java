package microservice_account.microservice_account.service.impl;

import lombok.RequiredArgsConstructor;
import microservice_account.microservice_account.dto.AccountFilter;
import microservice_account.microservice_account.model.Account;
import microservice_account.microservice_account.repository.AccountRepository;
import microservice_account.microservice_account.repository.AccountSpecification;
import microservice_account.microservice_account.service.AccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).get();
    }

    @Override
    public Account getAccountById(UUID id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public void markAccountAsDeletedById(UUID id) {
       Account account = accountRepository.findById(id).get();
       account.setDeleted(true);
       accountRepository.save(account);
    }

    @Override
    public void markAccountAsBlockedById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setBlocked(true);
        accountRepository.save(account);
    }

    @Override
    public void markAccountAsOfflineById(UUID id) {
        Account account = accountRepository.findById(id).get();
        account.setStatusCode("Offline");
        accountRepository.save(account);
    }

    @Override
    public Long getTotalAccountsCount() {
        return accountRepository.count();
    }

    @Override
    public List<Account> searchAccounts(AccountFilter accountFilter) {
        return accountRepository.findAll(AccountSpecification.withFilter(accountFilter),
                PageRequest.of(
                        accountFilter.getPageNumber(), accountFilter.getPageSize()
                )).getContent();
    }
}
