package com.develop.accountservice.mapper;

import org.mapstruct.Mapper;

import com.develop.accountservice.dto.request.RegisterRequest;
import com.develop.accountservice.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(RegisterRequest request);
}
