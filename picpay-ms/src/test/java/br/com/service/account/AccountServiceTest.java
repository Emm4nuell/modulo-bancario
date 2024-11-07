package br.com.service.account;

import br.com.entity.AccountCustomEntity;
import br.com.entity.AccountEntity;
import br.com.exception.AccountNumberNotFoundException;
import br.com.infrastructure.CheckNullValue;
import br.com.repository.AccountRepository;
import br.com.service.IAccountCustom;
import br.com.util.BalanceCheck;
import br.com.util.GenerateNumberAccount;
import br.com.util.ValidateAgency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class AccountServiceTest {

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
    private AccountNumberNotFoundException exception;
    @Mock
    private IAccountCustom iAccountCustom;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountCustomEntity accountCustom;

    private AccountEntity origin;
    private AccountEntity destination;
    private AccountEntity update;
    @Value(value = "${message.error.notAccount}")
    private String messageAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        init();
    }

    @Test
    @DisplayName("Faz o teste para criar uma nova conta para o usuario")
    void createAccount() {
        Mockito.when(accountRepository.save(origin)).thenReturn(origin);

        AccountEntity account = accountService.createAccount(origin);

        Assertions.assertEquals(1L, account.getId());
        Assertions.assertEquals("Maria Augusta da Silva", account.getName());
        Assertions.assertEquals("34351108487", account.getCpf());
        Assertions.assertEquals("Maria.augusta@gmail.com", account.getEmail());
        Assertions.assertEquals("1616", account.getAgencyNumber());
        Assertions.assertEquals(BigDecimal.ZERO, account.getBalance());
        Assertions.assertEquals("Gerente", account.getType());

        Assertions.assertNotNull(account);
        Assertions.assertNotNull(origin);

        Mockito.verify(accountRepository, Mockito.times(1)).save(origin);
        Mockito.verify(checkNullValue, Mockito.times(1)).check(origin);
        Mockito.verify(generateNumberAccount, Mockito.times(1)).generate();
        Mockito.verify(accountRepository, Mockito.times(1)).existsByAccountNumber(origin.getAccountNumber());
    }

    @Test
    @DisplayName("Testa a busca de todas as contas")
    void getAllAccounts(){
        Pageable pageable = PageRequest.of(0, 10);
        List<AccountEntity> accountList = Arrays.asList(origin, destination);
        Page<AccountEntity> accountPage = new PageImpl<>(accountList, pageable, accountList.size());

        Mockito.when(accountRepository.findAll(pageable)).thenReturn(accountPage);
        var accounts = accountService.getAllAccounts(pageable);

        Assertions.assertEquals(2, accounts.getTotalElements());
        Assertions.assertEquals(accountList, accounts.getContent());
    }

    @Test
    @DisplayName("Testa a busca pelo id: success")
    void getAccountById(){
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(origin));
        var account = accountService.getAccountById(1L);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(1L);

        Assertions.assertNotNull(account);
        Assertions.assertEquals(1L, account.getId());
        Assertions.assertEquals("Maria Augusta da Silva", account.getName());
        Assertions.assertEquals("34351108487", account.getCpf());
        Assertions.assertEquals("Maria.augusta@gmail.com", account.getEmail());
        Assertions.assertEquals("1616", account.getAgencyNumber());
        Assertions.assertEquals(BigDecimal.ZERO, account.getBalance());
        Assertions.assertEquals("Gerente", account.getType());
    }

    @Test
    @DisplayName("Faz a atualizacao dos dados da conta")
    void updateAccount() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(origin));
        Mockito.when(accountRepository.save(origin)).thenReturn(origin);

        AccountEntity account = accountService.updateAccount(update, 1L);

        Mockito.verify(checkNullValue, Mockito.times(1)).check(update, 1L);
        Assertions.assertNotNull(account);
        Assertions.assertEquals("Eduardo Emmanuel", account.getName());
        Assertions.assertEquals("eduardo.eesf@gmail.com", account.getEmail());
    }

    @Test
    @DisplayName("Teste para ser feito a transferencia de conta origem a conta destino")
    void transferValueAccount(){
        origin.setAccountNumber("123456");
        destination.setAccountNumber("654321");
        Mockito.when(accountRepository.findByAccountNumber(origin.getAccountNumber())).thenReturn(Optional.ofNullable(origin));
        Mockito.when(accountRepository.findByAccountNumber(destination.getAccountNumber())).thenReturn(Optional.ofNullable(destination));
        Mockito.when(accountRepository.save(origin)).thenReturn(origin);

        AccountEntity accountOrigin = accountService.transferValueAccount(origin, destination);

        Assertions.assertEquals(1L, origin.getId());
        Assertions.assertEquals("Maria Augusta da Silva", origin.getName());
        Assertions.assertEquals("34351108487", origin.getCpf());
        Assertions.assertEquals("Maria.augusta@gmail.com", origin.getEmail());
        Assertions.assertEquals("1616", origin.getAgencyNumber());
        Assertions.assertEquals(BigDecimal.ZERO, origin.getBalance());
        Assertions.assertEquals("Gerente", origin.getType());

        Assertions.assertEquals(2L, destination.getId());
        Assertions.assertEquals("Eduardo Emmanuel", destination.getName());
        Assertions.assertEquals("07687053478", destination.getCpf());
        Assertions.assertEquals("eduardo.eesf@gmail.com", destination.getEmail());
        Assertions.assertEquals("1616", destination.getAgencyNumber());
        Assertions.assertEquals(BigDecimal.ZERO, destination.getBalance());
        Assertions.assertEquals("Gerente", destination.getType());

    }

    @Test
    @DisplayName("Teste para deposito na conta")
    void depositValueAccount(){
        BigDecimal balance = BigDecimal.valueOf(1000);
        origin.setAccountNumber("123456");
        origin.setBalance(balance);
        Mockito.when(accountRepository.findByAccountNumber(origin.getAccountNumber())).thenReturn(Optional.ofNullable(origin));
        Mockito.when(accountRepository.save(origin)).thenReturn(origin);

        AccountEntity account = accountService.depositValueAccount(origin);

        Mockito.verify(checkNullValue, Mockito.times(1)).check(origin);

        Assertions.assertEquals(1L, account.getId());
        Assertions.assertEquals("Maria Augusta da Silva", account.getName());
        Assertions.assertEquals("34351108487", account.getCpf());
        Assertions.assertEquals("Maria.augusta@gmail.com", account.getEmail());
        Assertions.assertEquals("1616", account.getAgencyNumber());
        Assertions.assertEquals("123456", account.getAccountNumber());

        Assertions.assertEquals(BigDecimal.valueOf(1000).add(balance), account.getBalance());
        Assertions.assertEquals("Gerente", account.getType());
    }

    @Test
    @DisplayName("Teste para deleter conta")
    void deleteAccount(){
        Mockito.when(accountRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(accountRepository).deleteById(1L);
        accountService.deleteAccount(1L);
        Mockito.verify(accountRepository, Mockito.times(1)).existsById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Testa excepton quando nao localizar account pelo id")
    void exceptionGetAccountById(){
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        var excepton = Assertions.assertThrows(AccountNumberNotFoundException.class, () -> {
           accountService.getAccountById(1L);
        });
    }

    @Test
    @DisplayName("Teste de exception ao pesquisar se a conta existe na base de dados")
    void exceptionUpdateAccount(){
        Long id = 2L;
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        AccountNumberNotFoundException error = Assertions.assertThrows(AccountNumberNotFoundException.class, () -> {
            accountService.updateAccount(origin, id);
        });

        Assertions.assertEquals(messageAccount + " " + id, error.getMessage());
        Assertions.assertNotNull(error);
    }

    @Test
    @DisplayName("Testar exception que verifica o numero da conta")
    void exceptionTransferValueAccountNumber() {
        origin.setAccountNumber("123456");
        destination.setAccountNumber("654321");
        Mockito.when(accountRepository.findByAccountNumber(origin.getAccountNumber())).thenReturn(Optional.empty());

        AccountNumberNotFoundException errorOrigin = Assertions.assertThrows(AccountNumberNotFoundException.class, () -> {
            accountService.transferValueAccount(origin, destination);
        });
    }

    @Test
    @DisplayName("Teste para Exception ao pesquisar account para depositar")
    void exceptionDepositValueAccount(){
        origin.setAccountNumber("123456");
        Mockito.when(accountRepository.findByAccountNumber(origin.getAccountNumber())).thenReturn(Optional.empty());

        var error = Assertions.assertThrows(AccountNumberNotFoundException.class, () -> {
            accountService.depositValueAccount(origin);
        });

        Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber(origin.getAccountNumber());
        Assertions.assertEquals(messageAccount + " " + origin.getAccountNumber(), error.getMessage());
        Assertions.assertNotNull(error);
    }

    @Test
    @DisplayName("Teste excepton para deleter conta se a conte nao existir")
    void exceptionDeleteAccount(){
        Mockito.when(accountRepository.existsById(1L)).thenReturn(false);
        var error = Assertions.assertThrows(AccountNumberNotFoundException.class, () -> {
            accountService.deleteAccount(1L);
        });
    }

    void init(){
        origin = AccountEntity
                .builder()
                .id(1L)
                .name("Maria Augusta da Silva")
                .cpf("34351108487")
                .email("Maria.augusta@gmail.com")
                .agencyNumber("1616")
                .balance(BigDecimal.ZERO)
                .status(true)
                .accountCustom(accountCustom)
                .type("Gerente")
                .build();

        destination = AccountEntity
                .builder()
                .id(2L)
                .name("Eduardo Emmanuel")
                .cpf("07687053478")
                .email("eduardo.eesf@gmail.com")
                .agencyNumber("1616")
                .balance(BigDecimal.ZERO)
                .status(true)
                .accountCustom(accountCustom)
                .type("Gerente")
                .build();

        update = AccountEntity
                .builder()
                .name("Eduardo Emmanuel")
                .email("eduardo.eesf@gmail.com")
                .build();
    }

}