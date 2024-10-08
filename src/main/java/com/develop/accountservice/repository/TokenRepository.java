package com.develop.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.develop.accountservice.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {}
