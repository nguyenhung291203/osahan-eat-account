package com.develop.accountservice.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RegisterRequest {
    @Size(min = 10, max = 10,message = "Tài khoản phải đủ 10 kí tự")
    @NotBlank(message = "Tài khoản không được để trống")
    String username;

    @Size(min = 6, max = 50,message = "Mật khẩu phải có tối thiểu 6 ký tự")
    @NotBlank(message = "Mật khẩu không được để trống")
    String password;

    @Size(min = 6, max = 50,message = "Mật khẩu phải có tối thiểu 6 ký tự")
    @NotBlank(message = "Mật khẩu không được để trống")
    @JsonProperty("retype_password")
    String retypePassword;

    @Size(min = 6, max = 50,message = "Họ tên cần có tối đa 6 ký tự")
    @NotBlank(message = "Họ tên không được để trống")
    @JsonProperty("full_name")
    String fullName;

    boolean gender = true;

    @JsonProperty("date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String address;

    @JsonProperty("role_id")
    Integer roleId;
}
