package com.develop.accountservice.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.accountservice.dto.request.RoleCreationRequest;
import com.develop.accountservice.dto.response.ApiResponse;
import com.develop.accountservice.entity.Role;
import com.develop.accountservice.service.role.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Role>> createRole(@RequestBody @Valid RoleCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(roleService.createRole(request)));
    }
}
