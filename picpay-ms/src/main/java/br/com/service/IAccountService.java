package br.com.service;

import br.com.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IAccountService {
    AccountEntity createAccount(AccountEntity entity);
    Page<AccountEntity> getAllAccounts(Pageable pageable);
    AccountEntity getAccountById(Long id);
    AccountEntity updateAccount(AccountEntity entity, Long id);
    AccountEntity transferValueAccount(AccountEntity origin, AccountEntity destination);
    AccountEntity depositValueAccount(AccountEntity entity);
    void deleteAccount(Long id);
}
