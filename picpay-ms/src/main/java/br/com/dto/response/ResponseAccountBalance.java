package br.com.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseAccountBalance {
    private String name;
    private String cpf;
    private String agencyNumber;
    private String accountNumber;
    private BigDecimal balance;
}
