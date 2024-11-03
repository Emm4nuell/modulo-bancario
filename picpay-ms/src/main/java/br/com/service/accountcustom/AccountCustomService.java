package br.com.service.accountcustom;

import br.com.entity.AccountCustomEntity;
import br.com.entity.AccountEntity;
import br.com.exception.ValueTransactionException;
import br.com.repository.AccountCustomRepository;
import br.com.service.IAccountCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountCustomService implements IAccountCustom {

    private final AccountCustomRepository customRepository;

    @Override
    public AccountCustomEntity create() {
        return customRepository.save(AccountCustomEntity
                .builder()
                        .balanceMaxTransfer(BigDecimal.valueOf(1000))
                        .dailyLimit(BigDecimal.valueOf(1000))
                        .balanceTotalDaily(BigDecimal.ZERO)
                        .dailyLimitDate(LocalDateTime.now())
                .build());
    }

    @Override
    public void maxTransaction(AccountEntity account, AccountCustomEntity accountCustom) {
        var ts = accountCustom;
        var valor = account.getBalance();
        if(account.getBalance().compareTo(accountCustom.getBalanceMaxTransfer()) > 0){
            throw new ValueTransactionException("O valor fornecido e maior que o limite disponibilzado para transferencia. Conta:  " + account.getCpf() + "Limites: " + accountCustom);
        }
    }

    @Transactional
    @Override
    public void dailyTransaction(AccountEntity account, AccountCustomEntity accountCustom) {
        var totalBaily = accountCustom.getBalanceTotalDaily().add(account.getBalance());
        if (LocalDateTime.now().isAfter(accountCustom.getDailyLimitDate().plusHours(24))) {
            accountCustom.setBalanceTotalDaily(BigDecimal.ZERO.add(account.getBalance()));
        }else if(totalBaily.compareTo(accountCustom.getDailyLimit()) > 0){
            throw new ValueTransactionException("O valor fornecido e maior que o limite disponibilzado para transferencia. Valor:  " + account.getBalance() + "Limites: " + accountCustom);
        }
        accountCustom.setBalanceTotalDaily(totalBaily);
        customRepository.save(accountCustom);
    }
}
