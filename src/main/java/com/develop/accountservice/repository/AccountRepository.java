package com.develop.accountservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.develop.accountservice.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsAccountByUsername(String username);
    @Query("SELECT a FROM Account a WHERE a.profile.email = :email")
    Optional<Account> findByEmail(String email);
    Optional<Account> findAccountByUsername(String username);
}
