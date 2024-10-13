package com.develop.accountservice.constant.error;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TokenErrorCode implements BaseErrorCode {
    TOKEN_NOT_FOUND(5001, "Token không tồn tại", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(5002, "Refresh token đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(5003, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(5004, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN(5005, "Token không được hỗ trợ", HttpStatus.UNAUTHORIZED),
    EMPTY_TOKEN(5006, "Chuỗi JWT trống", HttpStatus.BAD_REQUEST),
    EMPTY_REVOKED(5007, "Token đã bị vô hiệu hóa", HttpStatus.UNAUTHORIZED);
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
