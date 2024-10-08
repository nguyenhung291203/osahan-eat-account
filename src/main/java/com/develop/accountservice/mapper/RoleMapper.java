package com.develop.accountservice.mapper;

import org.mapstruct.Mapper;

import com.develop.accountservice.dto.request.RoleCreationRequest;
import com.develop.accountservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleCreationRequest request);
}
