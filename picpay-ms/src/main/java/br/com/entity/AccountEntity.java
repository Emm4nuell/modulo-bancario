package br.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String type;
    @NotBlank(message = "Campo AGENCY e obrigatorio.")
    private String agencyNumber;
    private String accountNumber;
    private boolean status;
//    @NotBlank(message = "${message.error.balance}")
    private BigDecimal balance;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_custom_id")
    private AccountCustomEntity accountCustom;
}
