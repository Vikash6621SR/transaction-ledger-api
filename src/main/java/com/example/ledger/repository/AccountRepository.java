package com.example.ledger.repository;

import com.example.ledger.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(
        value = "SELECT * FROM accounts WHERE id = :id FOR UPDATE",
        nativeQuery = true
    )
    Optional<Account> findByIdForUpdate(@Param("id") Long id);
}
