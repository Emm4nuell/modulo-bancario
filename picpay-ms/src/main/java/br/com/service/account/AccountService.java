package br.com.service.account;

import br.com.entity.AccountEntity;
import br.com.exception.AccountNumberNotFoundException;
import br.com.infrastructure.CheckNullValue;
import br.com.repository.AccountRepository;
import br.com.service.IAccountCustom;
import br.com.service.IAccountService;
import br.com.util.ValidateAgency;
import br.com.util.GenerateNumberAccount;
import br.com.util.BalanceCheck;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final CheckNullValue checkNullValue;
    private final GenerateNumberAccount generateNumberAccount;
    private final BalanceCheck balanceCheck;
    private final ValidateAgency validateAgency;
    private final IAccountCustom iAccountCustom;

    @Value(value = "${message.error.notAccount}")
    private String messageAccount;

    @Transactional
    @Override
    public AccountEntity createAccount(AccountEntity entity) {
        checkNullValue.check(entity);

        String account;
        do {
            account = generateNumberAccount.generate();
        }while (accountRepository.existsByAccountNumber(account));

        entity.setAccountNumber(account);
        entity.setBalance(BigDecimal.ZERO);
        entity.setAccountCustom(iAccountCustom.create());

        //VERIFICAR SE EXISTE ALGUMA RESTRIÇAO NO CPF DO CLIENTE PARA PODER ATIVAR OU NAO A ACCOUNT

        entity.setStatus(false);
        return accountRepository.save(entity);
    }

    @Override
    public Page<AccountEntity> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public AccountEntity getAccountById(Long id) {
        checkNullValue.check(id);
        return accountRepository.findById(id).orElseThrow(() ->
                new AccountNumberNotFoundException(messageAccount + " " + id));
    }

    @Transactional
    @Override
    public AccountEntity updateAccount(AccountEntity entity, Long id) {
        checkNullValue.check(entity, id);
        var account = accountRepository.findById(id).orElseThrow(() ->
                new AccountNumberNotFoundException(messageAccount + " " + id));
        account.setName(entity.getName());
        account.setEmail(entity.getEmail());
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public AccountEntity transferValueAccount(AccountEntity originRequest, AccountEntity destination) {

        var accountOrigin = accountRepository.findByAccountNumber(originRequest.getAccountNumber())
                .orElseThrow(() ->
                        new AccountNumberNotFoundException(messageAccount + " " + originRequest.getAccountNumber()));
        var accountDestination = accountRepository.findByAccountNumber(destination.getAccountNumber())
                .orElseThrow(() ->
                        new AccountNumberNotFoundException(messageAccount + " " + destination.getAccountNumber()));

        validateAgency.validate(destination.getAgencyNumber(), accountDestination.getAgencyNumber());

        iAccountCustom.maxTransaction(originRequest, accountOrigin.getAccountCustom());
        iAccountCustom.dailyTransaction(originRequest, accountOrigin.getAccountCustom());

        balanceCheck.updateBalance(accountOrigin, accountDestination, originRequest.getBalance());

        accountRepository.save(accountDestination);
        return accountRepository.save(accountOrigin);
    }

    @Override
    public AccountEntity depositValueAccount(AccountEntity entity) {
        checkNullValue.check(entity);
        var account = accountRepository.findByAccountNumber(entity.getAccountNumber())
                .orElseThrow(() ->
                new AccountNumberNotFoundException(messageAccount + " " + entity.getAccountNumber()));
        validateAgency.validate(entity.getAgencyNumber(), account.getAgencyNumber());

        account.setBalance(account.getBalance().add(entity.getBalance()));

        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        checkNullValue.check(id);
        if (!accountRepository.existsById(id)){
            throw new AccountNumberNotFoundException(messageAccount + " " + id);
        }
        accountRepository.deleteById(id);
    }
}
