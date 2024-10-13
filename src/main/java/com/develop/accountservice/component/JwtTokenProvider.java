package com.develop.accountservice.component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.develop.accountservice.constant.error.TokenErrorCode;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Token;
import com.develop.accountservice.exception.ApiException;
import com.develop.accountservice.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    long jwtExpirationDate;

    final TokenRepository tokenRepository;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(Account account) {
        String id = account.getId();
        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    public String getAccountId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    boolean isTokenExpired(String token) {
        Token tokenEntity =
                tokenRepository.findByToken(token).orElseThrow(() -> new ApiException(TokenErrorCode.TOKEN_NOT_FOUND));
        return tokenEntity.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
