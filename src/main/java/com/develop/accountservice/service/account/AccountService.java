package com.develop.accountservice.service.account;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import com.develop.accountservice.dto.request.LoginRequest;
import com.develop.accountservice.dto.request.LogoutRequest;
import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.AccountInfoResponse;
import com.develop.accountservice.dto.response.RegisterResponse;
import com.develop.accountservice.dto.response.TokenResponse;

public interface AccountService {
    RegisterResponse register(RegisterRequest registerRequest);

    AccountInfoResponse getInfo();

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse oauth2Login(OAuth2AuthenticationToken authentication);

    void logout(LogoutRequest registerRequest);

    AccountInfoResponse uploadAvatar(String accountId, MultipartFile file) throws IOException;

    Resource loadAvatar(String filename) throws MalformedURLException;
}
