package com.develop.accountservice.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.develop.accountservice.dto.request.LoginRequest;
import com.develop.accountservice.dto.request.LogoutRequest;
import com.develop.accountservice.dto.request.RefreshTokenRequest;
import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.AccountInfoResponse;
import com.develop.accountservice.dto.response.ApiResponse;
import com.develop.accountservice.dto.response.RegisterResponse;
import com.develop.accountservice.dto.response.TokenResponse;
import com.develop.accountservice.service.account.AccountService;
import com.develop.accountservice.service.token.TokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;
    TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Đăng ký thành công", accountService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Đăng nhập thành công", accountService.login(request)));
    }

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<ApiResponse<TokenResponse>> oauth2Login(
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
            OAuth2AuthenticationToken authentication) {
        return ResponseEntity.ok(ApiResponse.ok(accountService.oauth2Login(authentication)));
    }

    @GetMapping("/my-info")
    public ResponseEntity<ApiResponse<AccountInfoResponse>> getMyInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(accountService.getInfo()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid LogoutRequest request) {
        accountService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.deleted("Đăng xuất thành công"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(tokenService.refreshToken(request)));
    }

    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity<ApiResponse<Object>> uploadAvatar(
            @PathVariable @Valid String id, @RequestParam("file") @Valid MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(accountService.uploadAvatar(id, file)));
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewAvatar(@PathVariable String fileName) throws MalformedURLException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(accountService.loadAvatar(fileName));
    }
}
