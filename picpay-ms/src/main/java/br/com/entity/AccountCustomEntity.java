package br.com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_custom")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal dailyLimit;
    private BigDecimal balanceTotalDaily;
    private BigDecimal balanceMaxTransfer;
    private LocalDateTime dailyLimitDate;
}
