package com.develop.accountservice.service.role;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.develop.accountservice.constant.error.RoleErrorCode;
import com.develop.accountservice.dto.request.RoleCreationRequest;
import com.develop.accountservice.entity.Role;
import com.develop.accountservice.exception.ApiException;
import com.develop.accountservice.mapper.RoleMapper;
import com.develop.accountservice.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role createRole(RoleCreationRequest request) {
        if (roleRepository.existsByName(request.getName()) || roleRepository.existsByCode(request.getCode())) {
            throw new ApiException(RoleErrorCode.ROLE_ALREADY_EXISTS);
        }
        Role role = roleMapper.toRole(request);
        return roleRepository.save(role);
    }
}
