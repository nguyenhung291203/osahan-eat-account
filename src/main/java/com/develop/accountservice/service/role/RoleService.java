package com.develop.accountservice.service.role;

import java.util.List;

import com.develop.accountservice.dto.request.RoleCreationRequest;
import com.develop.accountservice.entity.Role;

public interface RoleService {
    List<Role> findAllRoles();

    Role createRole(RoleCreationRequest request);
}
