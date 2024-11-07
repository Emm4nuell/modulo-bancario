package br.com.repository;

import br.com.entity.AccountCustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCustomRepository extends JpaRepository<AccountCustomEntity, Long> {
}
