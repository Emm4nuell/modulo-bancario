package br.com.repository;

import br.com.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    boolean existsByAccountNumber(String accountNumber);

    @Query(value = "SELECT * FROM account AS a WHERE a.account_number = :account_number", nativeQuery = true)
    Optional<AccountEntity> findByAccountNumber(@Param("account_number") String account_number);

}
