package com.develop.accountservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findAllByAccount(Account account);

    Optional<Token> findTokenByRefreshToken(String refreshToken);

    boolean existsByToken(String token);

    Optional<Token> findByToken(String token);
}
