package com.microservice.service.impl;

import com.microservice.repository.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import com.microservice.dto.AccountFilter;
import com.microservice.model.Account;
import com.microservice.repository.AccountRepository;
import com.microservice.repository.AccountSpecification;
import com.microservice.service.AccountService;
import com.microservice.utils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
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
       accountRepository.deleteById(id);
        //удали строчку выше !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
                        accountFilter.getPage(), accountFilter.getSize()
                )).getContent();
    }

    public Page<Account> findAccount(Boolean isDeleted, Integer size) {
        Specification<Account> spec = Specification.where(null);
        if (isDeleted != null) {
            spec = spec.and(AccountSpecifications.byIsDeleted(isDeleted));
        }
        return accountRepository.findAll(spec, PageRequest.of(1, size));
    }





    public Account update(Account account) {
        Account existedAccount = accountRepository.findById(account.getId()).get();
        BeanUtils.copyNonNullProperties(account, existedAccount);
        return accountRepository.save(existedAccount);
    }



}
