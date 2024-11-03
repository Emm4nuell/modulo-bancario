package br.com.application;

import br.com.dto.request.RequesAccountTransfer;
import br.com.dto.request.RequestAccountDeposit;
import br.com.dto.response.ResponseAccountBalance;
import br.com.dto.request.RequestAccount;
import br.com.dto.response.ResponseAccount;
import br.com.entity.AccountEntity;
import br.com.service.IAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ApiAccountApplication implements IApiAccountApplication{

    private final IAccountService accountService;
    private final ObjectMapper mapper;

    @Override
    public ResponseEntity<Void> create(RequestAccount request) {
        var entity = accountService.createAccount(mapper.convertValue(request, AccountEntity.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<ResponseAccount> updateAccount(RequestAccount request, Long id) {
        var account= accountService.updateAccount(mapper.convertValue(request, AccountEntity.class), id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(account, ResponseAccount.class));
    }

    @Override
    public ResponseEntity<ResponseAccountBalance> transfer(RequesAccountTransfer requestAccount) {

        var originEntity = AccountEntity
                .builder()
                .agencyNumber(requestAccount.getAgencyOrigin())
                .accountNumber(requestAccount.getAccountOrigin())
                .balance(requestAccount.getBalanceDestination())
                .build();
        var destinationEntity = AccountEntity
                .builder()
                .agencyNumber(requestAccount.getAgencyDestination())
                .accountNumber(requestAccount.getAccountDestination())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(accountService.transferValueAccount(originEntity, destinationEntity), ResponseAccountBalance.class));
    }

    @Override
    public ResponseEntity<ResponseAccountBalance> deposit(RequestAccountDeposit account) {
        var entity = accountService.depositValueAccount(mapper.convertValue(account, AccountEntity.class));
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(entity, ResponseAccountBalance.class));
    }

    @Override
    public ResponseEntity<ResponseAccount> getAccount(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(accountService.getAccountById(id), ResponseAccount.class));
    }

    @Override
    public ResponseEntity<Page<ResponseAccount>> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(direction), sort));
        var accounts = accountService.getAllAccounts(pageable);
        var content = accounts.stream().map(e -> mapper.convertValue(e, ResponseAccount.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(new PageImpl<>(content, pageable, accounts.getTotalElements()));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
