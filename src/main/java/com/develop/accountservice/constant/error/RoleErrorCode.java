package com.develop.accountservice.constant.error;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RoleErrorCode implements BaseErrorCode {
    ROLE_NOT_FOUND(2001, "Vai trò không tồn tại", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS(2002, "Vai trò đã tồn tại", HttpStatus.CONFLICT),
    INVALID_ROLE_NAME(2003, "Tên vai trò không hợp lệ", HttpStatus.BAD_REQUEST),
    ROLE_ASSIGNMENT_FAILED(2004, "Phân quyền thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    ROLE_ACCESS_DENIED(2005, "Bạn không có quyền truy cập vai trò này", HttpStatus.FORBIDDEN);

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
