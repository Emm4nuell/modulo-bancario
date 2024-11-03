package br.com.infrastructure;

import br.com.exception.NullValueException;
import org.springframework.stereotype.Component;

@Component
public class CheckNullValue {
    public void check(Object... obj){
        for (Object value: obj) {
            if (value == null){
                throw new NullValueException("O Campo nao pode ser nulo. ");
            }
        }
    }
}
