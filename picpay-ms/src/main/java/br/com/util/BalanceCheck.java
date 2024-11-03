package br.com.util;

import br.com.entity.AccountEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceCheck {

    public void updateBalance(AccountEntity origin, AccountEntity destination, BigDecimal value){
        if (value.compareTo(origin.getBalance()) <= 0){
            origin.setBalance(origin.getBalance().subtract(value));
            destination.setBalance(destination.getBalance().add(value));
        }else{
            throw new NullPointerException("Saldo insuficiente.");
        }
    }
}
