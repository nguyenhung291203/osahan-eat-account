package com.develop.accountservice.service.account;

import com.develop.accountservice.constant.RoleCode;
import com.develop.accountservice.constant.error.AccountErrorCode;
import com.develop.accountservice.constant.error.RoleErrorCode;
import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.RegisterResponse;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Profile;
import com.develop.accountservice.entity.Role;
import com.develop.accountservice.exception.ApiException;
import com.develop.accountservice.mapper.AccountMapper;
import com.develop.accountservice.mapper.ProfileMapper;
import com.develop.accountservice.repository.AccountRepository;
import com.develop.accountservice.repository.ProfileRepository;
import com.develop.accountservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {
    RoleRepository roleRepository;
    AccountRepository accountRepository;
    ProfileMapper profileMapper;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    private Role getRoleById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new ApiException(RoleErrorCode.ROLE_NOT_FOUND));
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
}
