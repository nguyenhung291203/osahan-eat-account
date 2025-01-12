package com.develop.accountservice.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginRequest {
    @NotBlank(message = "Tài khoản không được để trống")
    String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    String password;
}
