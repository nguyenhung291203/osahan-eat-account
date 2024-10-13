package com.develop.accountservice.mapper;

import org.mapstruct.Mapper;

import com.develop.accountservice.dto.response.TokenResponse;
import com.develop.accountservice.entity.Token;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenResponse toTokenResponse(Token token);
}
