package com.develop.accountservice.service.token;

import com.develop.accountservice.dto.request.RefreshTokenRequest;
import com.develop.accountservice.dto.response.TokenResponse;
import com.develop.accountservice.entity.Token;

public interface TokenService {
    Token addToken(String accountId, String token);

    TokenResponse refreshToken(RefreshTokenRequest request);
}
