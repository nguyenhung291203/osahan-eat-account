package com.develop.accountservice.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.develop.accountservice.constant.error.AccountErrorCode;
import com.develop.accountservice.constant.error.BaseErrorCode;
import com.develop.accountservice.constant.error.TokenErrorCode;
import com.develop.accountservice.dto.response.ApiResponse;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Token;
import com.develop.accountservice.exception.ApiException;
import com.develop.accountservice.repository.AccountRepository;
import com.develop.accountservice.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    final JwtTokenProvider jwtTokenProvider;
    final UserDetailsService userDetailsService;
    final AccountRepository accountRepository;
    final TokenRepository tokenRepository;

    @Value("${api.prefix}")
    String apiPrefix;

    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        ApiResponse<Object> apiResponse = new ApiResponse<>(errorCode.getCode(), errorCode.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getTokenFromRequest(request);
        if (StringUtils.hasText(token)) {
            Token tokenEntity = tokenRepository.findByToken(token).orElse(null);
            if (tokenEntity == null) {
                sendErrorResponse(response, TokenErrorCode.TOKEN_NOT_FOUND);
                return;
            }

            if (Boolean.TRUE.equals(tokenEntity.isRevoked())) {
                sendErrorResponse(response, TokenErrorCode.EMPTY_REVOKED);
                return;
            }

            if (jwtTokenProvider.isTokenExpired(token)) {
                sendErrorResponse(response, TokenErrorCode.TOKEN_EXPIRED);
                return;
            }

            String accountId = jwtTokenProvider.getAccountId(token);
            Account account = accountRepository
                    .findById(accountId)
                    .orElseThrow(() -> new ApiException(AccountErrorCode.USERNAME_EMPTY));
            if (!account.isActive()) {
                sendErrorResponse(response, AccountErrorCode.ACCOUNT_LOCKED);
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUsername());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/accounts/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/accounts/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/accounts/logout", apiPrefix), "POST"),
                Pair.of(String.format("%s/accounts/view", apiPrefix), "GET"));
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().startsWith(bypassToken.getLeft())
                    && request.getMethod().equalsIgnoreCase(bypassToken.getRight())) {
                return true;
            }
        }

        return false;
    }
}
