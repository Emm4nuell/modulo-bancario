package br.com.service.account;

import br.com.entity.AccountEntity;
import br.com.infrastructure.CheckNullValue;
import br.com.repository.AccountRepository;
import br.com.service.IAccountCustom;
import br.com.util.BalanceCheck;
import br.com.util.GenerateNumberAccount;
import br.com.util.ValidateAgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CheckNullValue checkNullValue;
    @Mock
    private GenerateNumberAccount generateNumberAccount;
    @Mock
    private BalanceCheck balanceCheck;
    @Mock
    private ValidateAgency validateAgency;
    @Mock
    private IAccountCustom iAccountCustom;

    private AccountEntity request;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Verifica se a conta REQUEST existe na base de dados")
    void verifiAccountRequestExistis() {
        Mockito.when(accountService.createAccount(Mockito.any())).thenReturn(request);
    }

}