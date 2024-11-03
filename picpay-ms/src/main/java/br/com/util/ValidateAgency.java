package br.com.util;

import br.com.exception.AccountNumberNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ValidateAgency {
    public void validate(String request, String origin){
        if (!request.equals(origin)){
            throw new AccountNumberNotFoundException("Numero da agencia esta incorreto. " +request);
        }
    }
}
