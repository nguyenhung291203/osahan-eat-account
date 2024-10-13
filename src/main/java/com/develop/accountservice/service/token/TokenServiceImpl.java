package com.develop.accountservice.service.token;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.develop.accountservice.component.JwtTokenProvider;
import com.develop.accountservice.constant.error.AccountErrorCode;
import com.develop.accountservice.constant.error.TokenErrorCode;
import com.develop.accountservice.dto.request.RefreshTokenRequest;
import com.develop.accountservice.dto.response.TokenResponse;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Token;
import com.develop.accountservice.exception.ApiException;
import com.develop.accountservice.mapper.TokenMapper;
import com.develop.accountservice.repository.AccountRepository;
import com.develop.accountservice.repository.TokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenServiceImpl implements TokenService {
    final TokenRepository tokenRepository;
    final AccountRepository accountRepository;
    final JwtTokenProvider jwtTokenProvider;
    final TokenMapper tokenMapper;

    @Value("${app.jwt-expiration-milliseconds}")
    long jwtExpirationDate;

    @Value("${app.jwt-expiration-refresh-token}")
    long jwtExpirationRefreshToken;

    private static final int MAX_TOKENS = 3;

    private Account getAccountById(String accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new ApiException(AccountErrorCode.ACCOUNT_NOT_EXISTED));
    }

    private Token getTokenByRefreshToken(String refreshToken) {
        return tokenRepository
                .findTokenByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApiException(TokenErrorCode.TOKEN_NOT_FOUND));
    }

    @Override
    @Transactional
    public Token addToken(String accountId, String token) {
        Account account = getAccountById(accountId);
        List<Token> tokens = tokenRepository.findAllByAccount(account);

        if (tokens.size() >= MAX_TOKENS) {
            tokenRepository.delete(tokens.getLast());
        }

        Token tokenEntity = Token.builder()
                .account(account)
                .token(token)
                .refreshToken(UUID.randomUUID().toString())
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expiresAt(LocalDateTime.now().plusSeconds(jwtExpirationDate / 1000))
                .refreshTokenExpiresAt(LocalDateTime.now().plusSeconds(jwtExpirationRefreshToken / 1000))
                .build();
        return tokenRepository.save(tokenEntity);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        Token token = getTokenByRefreshToken(request.getRefreshToken());
        Account account = getAccountById(token.getAccount().getId());
        if (token.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new ApiException(TokenErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        token.setToken(jwtTokenProvider.generateToken(account));
        token.setExpiresAt(LocalDateTime.now().plusSeconds(jwtExpirationDate));
        token.setRefreshTokenExpiresAt(LocalDateTime.now().plusSeconds(jwtExpirationRefreshToken));
        token.setRefreshToken(UUID.randomUUID().toString());

        return tokenMapper.toTokenResponse(token);
    }
}
