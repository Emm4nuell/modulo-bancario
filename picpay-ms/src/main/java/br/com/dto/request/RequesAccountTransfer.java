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
public class RequesAccountTransfer {
    private String agencyOrigin;
    private String accountOrigin;
    private String agencyDestination;
    private String accountDestination;
    private BigDecimal balanceDestination;
}
