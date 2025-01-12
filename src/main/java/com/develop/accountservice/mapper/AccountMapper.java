package com.develop.accountservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.develop.accountservice.config.AppConfig;
import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.dto.response.AccountInfoResponse;
import com.develop.accountservice.dto.response.RegisterResponse;
import com.develop.accountservice.entity.Account;
import com.develop.accountservice.entity.Profile;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {
    @Autowired
    protected AppConfig appConfig;

    public abstract Account toAccount(RegisterRequest request);

    public abstract Profile toProfile(RegisterRequest request);

    @Mapping(source = "account.id", target = "id")
    public abstract RegisterResponse toRegisterResponse(Account account, Profile profile);

    @Mapping(source = "account.id", target = "id")
    public abstract AccountInfoResponse toAccountInfoResponse(Account account, Profile profile);

    @AfterMapping
    protected void mapAvatarUrl(Profile profile, @MappingTarget AccountInfoResponse response) {
        if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
            response.setAvatar(appConfig.getBaseUrl() + "/view/" + profile.getAvatar());
        }
    }

    @AfterMapping
    protected void mapAvatarUrlToRegisterResponse(Profile profile, @MappingTarget RegisterResponse response) {
        if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
            response.setAvatar(appConfig.getBaseUrl() + "/view/" + profile.getAvatar());
        }
    }
}
