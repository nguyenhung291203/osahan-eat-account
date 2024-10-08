package com.develop.accountservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.RegisterResponse;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Profile;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(RegisterRequest request);

    Profile toProfile(RegisterRequest request);

    @Mapping(source = "account.id", target = "id")
    RegisterResponse toRegisterResponse(Account account, Profile profile);
}
