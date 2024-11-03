package br.com.application;

import br.com.dto.request.RequesAccountTransfer;
import br.com.dto.request.RequestAccount;
import br.com.dto.request.RequestAccountDeposit;
import br.com.dto.response.ResponseAccount;
import br.com.dto.response.ResponseAccountBalance;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("account/")
@CrossOrigin("*")
public interface IApiAccountApplication {
    @PostMapping("create")
    ResponseEntity<Void> create(@RequestBody RequestAccount request);

    @PutMapping("update/{id}")
    ResponseEntity<ResponseAccount> updateAccount(@RequestBody RequestAccount request, @PathVariable("id") Long id);

    @PostMapping("transfer")
    ResponseEntity<ResponseAccountBalance> transfer(@RequestBody RequesAccountTransfer requestAccount);

    @PutMapping("deposit")
    ResponseEntity<ResponseAccountBalance> deposit(@RequestBody RequestAccountDeposit account);

    @GetMapping("get/{id}")
    ResponseEntity<ResponseAccount> getAccount(@PathVariable("id") Long id);

    @GetMapping("getall")
    ResponseEntity<Page<ResponseAccount>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction);

    @DeleteMapping("delete/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);
}
