package com.develop.accountservice.service.account;

import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.RegisterResponse;

public interface AccountService {
    RegisterResponse register(RegisterRequest registerRequest);
}
