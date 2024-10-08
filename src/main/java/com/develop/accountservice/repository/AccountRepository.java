package com.develop.accountservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.develop.accountservice.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsAccountByUsername(String username);

    Optional<Account> findAccountByUsername(String username);
}
