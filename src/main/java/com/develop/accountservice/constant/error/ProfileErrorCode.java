package com.develop.accountservice.constant.error;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ProfileErrorCode implements BaseErrorCode {
    PROFILE_NOT_FOUND(4001, "Hồ sơ không tồn tại", HttpStatus.NOT_FOUND),
    PROFILE_UPDATE_FAILED(4002, "Cập nhật hồ sơ không thành công", HttpStatus.BAD_REQUEST),
    INVALID_PROFILE_DATA(4003, "Dữ liệu hồ sơ không hợp lệ", HttpStatus.BAD_REQUEST),
    PROFILE_EXISTED(4004, "Hồ sơ đã tồn tại", HttpStatus.BAD_REQUEST),
    IMAGE_EMPTY(4005, "Avatar không được để trống", HttpStatus.BAD_REQUEST),
    IMAGE_LOAD_FAILED(4006, "Tải ảnh không thành công", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_TOO_LARGE(4007, "Kích thước ảnh vượt quá giới hạn cho phép (tối đa 10MB)", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_FORMAT(
            4008, "Định dạng file ảnh không hợp lệ. Chỉ chấp nhận JPG, PNG, JPEG, GIF.", HttpStatus.BAD_REQUEST);
    ;

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
