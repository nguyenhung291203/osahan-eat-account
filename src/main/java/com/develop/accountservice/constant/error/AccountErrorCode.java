package com.develop.accountservice.constant.error;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AccountErrorCode implements BaseErrorCode {
    ACCOUNT_EXISTED(3001, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTED(3002, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(3003, "Tên đăng nhập không hợp lệ", HttpStatus.BAD_REQUEST),
    USERNAME_EMPTY(3004, "Tên người dùng không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(3005, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD(3006, "Tài khoản hoặc mật khẩu không chính xác", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED(3007, "Tài khoản đã bị khóa", HttpStatus.FORBIDDEN);
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
