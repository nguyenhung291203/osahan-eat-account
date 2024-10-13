package com.develop.accountservice.service.account;

import com.develop.accountservice.component.JwtTokenProvider;
import com.develop.accountservice.constant.RoleCode;
import com.develop.accountservice.constant.error.AccountErrorCode;
import com.develop.accountservice.constant.error.ProfileErrorCode;
import com.develop.accountservice.constant.error.RoleErrorCode;
import com.develop.accountservice.constant.error.TokenErrorCode;
import com.develop.accountservice.dto.request.LoginRequest;
import com.develop.accountservice.dto.request.LogoutRequest;
import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.AccountInfoResponse;
import com.develop.accountservice.dto.response.RegisterResponse;
import com.develop.accountservice.dto.response.TokenResponse;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Profile;
import com.develop.accountservice.entity.Role;
import com.develop.accountservice.entity.Token;
import com.develop.accountservice.exception.ApiException;
import com.develop.accountservice.mapper.AccountMapper;
import com.develop.accountservice.mapper.ProfileMapper;
import com.develop.accountservice.mapper.TokenMapper;
import com.develop.accountservice.repository.AccountRepository;
import com.develop.accountservice.repository.ProfileRepository;
import com.develop.accountservice.repository.RoleRepository;
import com.develop.accountservice.repository.TokenRepository;
import com.develop.accountservice.service.token.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountServiceImpl implements AccountService {
    final RoleRepository roleRepository;
    final AccountRepository accountRepository;
    final ProfileRepository profileRepository;
    final TokenRepository tokenRepository;
    final ProfileMapper profileMapper;
    final AccountMapper accountMapper;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final TokenService tokenService;
    final JwtTokenProvider jwtTokenProvider;
    final TokenMapper tokenMapper;

    @Value("${upload.path}")
    String uploadPath;

    @Value("${upload.max-file-size}")
    Long maxFileSize;

    private Role getRoleById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new ApiException(RoleErrorCode.ROLE_NOT_FOUND));
    }

    private Account getAccountByUserName(String username) {
        return accountRepository
                .findAccountByUsername(username)
                .orElseThrow(() -> new ApiException(AccountErrorCode.ACCOUNT_NOT_EXISTED));
    }

    private Profile getProfileById(String id) {
        return profileRepository.findById(id).orElseThrow(() -> new ApiException(AccountErrorCode.ACCOUNT_NOT_EXISTED));
    }

    private Token getTokenByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new ApiException(TokenErrorCode.TOKEN_NOT_FOUND));
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (accountRepository.existsAccountByUsername(request.getUsername())) {
            throw new ApiException(AccountErrorCode.ACCOUNT_EXISTED);
        }

        if (!request.getPassword().equals(request.getRetypePassword())) {
            throw new ApiException(AccountErrorCode.INCORRECT_PASSWORD);
        }

        request.setRoleId(
                request.getRoleId() == null
                        ? roleRepository.findByCode(RoleCode.CUSTOMER).getId()
                        : request.getRoleId());
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = getRoleById(request.getRoleId());
        account.setRole(role);
        account.setActive(true);
        accountRepository.save(account);
        Profile profile = profileMapper.toProfile(request);
        profile.setDateOfBirth(LocalDate.now());
        profile.setAccount(account);

        profileRepository.save(profile);

        return accountMapper.toRegisterResponse(account, profile);
    }

    @Override
    public AccountInfoResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        Account account = getAccountByUserName(username);
        Profile profile = profileRepository.findById(account.getId()).orElse(null);
        return accountMapper.toAccountInfoResponse(account, profile);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Account account = accountRepository
                .findAccountByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApiException(AccountErrorCode.INCORRECT_PASSWORD));
        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new ApiException(AccountErrorCode.INCORRECT_PASSWORD);
        }

        if (!account.isActive()) {
            throw new ApiException(AccountErrorCode.ACCOUNT_LOCKED);
        }

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Token token = tokenService.addToken(account.getId(), jwtTokenProvider.generateToken(account));

        return tokenMapper.toTokenResponse(token);
    }

    @Override
    public TokenResponse oauth2Login(OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String provider = authentication.getAuthorizedClientRegistrationId();

        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    RegisterRequest request = new RegisterRequest();
                    request.setUsername(email);
                    request.setPassword(UUID.randomUUID().toString());
                    request.setRoleId(roleRepository.findByCode(RoleCode.CUSTOMER).getId());
                    return accountMapper.toAccount(request);
                });

        Token token = tokenService.addToken(account.getId(), jwtTokenProvider.generateToken(account));
        return tokenMapper.toTokenResponse(token);
    }

    @Override
    public void logout(LogoutRequest registerRequest) {
        Token token = getTokenByToken(registerRequest.getToken());
        token.setRevoked(true);
        tokenRepository.save(token);
        SecurityContextHolder.clearContext();
    }

    @Override
    public AccountInfoResponse uploadAvatar(String accountId, MultipartFile file) throws IOException {
        Profile profile = getProfileById(accountId);
        if (file.isEmpty()) {
            throw new ApiException(ProfileErrorCode.IMAGE_EMPTY);
        }
        String contentType = file.getContentType();
        if (contentType == null
                || !(contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/gif"))) {
            throw new ApiException(ProfileErrorCode.INVALID_IMAGE_FORMAT);
        }

        if (file.getSize() > maxFileSize) {
            throw new ApiException(ProfileErrorCode.IMAGE_TOO_LARGE);
        }

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        profile.setAvatar(fileName);
        profile = profileRepository.save(profile);
        return accountMapper.toAccountInfoResponse(profile.getAccount(), profile);
    }

    @Override
    public Resource loadAvatar(String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }
        throw new ApiException(ProfileErrorCode.IMAGE_LOAD_FAILED);
    }
}
