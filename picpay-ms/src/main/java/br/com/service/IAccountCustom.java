package br.com.service;

import br.com.entity.AccountCustomEntity;
import br.com.entity.AccountEntity;

public interface IAccountCustom {
    AccountCustomEntity create();
    void maxTransaction(AccountEntity account, AccountCustomEntity accountCustom);
    void dailyTransaction(AccountEntity account, AccountCustomEntity accountCustom);
}
