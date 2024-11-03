package br.com.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestAccountDeposit {
    private String agencyNumber;
    private String accountNumber;
    private BigDecimal balance;
}
