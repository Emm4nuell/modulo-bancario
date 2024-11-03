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
public class ResponseAccount {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String type;
    private String agencyNumber;
    private String accountNumber;
    private boolean status;
    private BigDecimal balance;
}
