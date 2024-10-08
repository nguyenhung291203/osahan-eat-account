package com.develop.accountservice.exception;

import com.develop.accountservice.constant.error.BaseErrorCode;
import com.develop.accountservice.constant.error.GlobalErrorCode;
import com.develop.accountservice.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ApiResponse<Void> generateExceptionResponse(BaseErrorCode errorCode) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return apiResponse;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex) {
        BaseErrorCode errorCode = ex.getErrorCode();
        ApiResponse<Void> apiExceptionResponse = generateExceptionResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiExceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.DATA_VALIDATION_FAILED;
        ApiResponse<Void> apiExceptionResponse = generateExceptionResponse(errorCode);
        apiExceptionResponse.setMessage(
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiExceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_DATA_FORMAT;
        ApiResponse<Void> apiExceptionResponse = generateExceptionResponse(errorCode);
        String errorMessage = "Dữ liệu không hợp lệ: " + ex.getMessage();
        Throwable rootCause = ex.getCause();
        if (rootCause instanceof JsonMappingException jsonMappingException) {
            String fieldName = jsonMappingException.getPath().getFirst().getFieldName();
            errorMessage = "Dữ liệu không hợp lệ cho trường: " + fieldName;
        }

        apiExceptionResponse.setMessage(errorMessage);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiExceptionResponse);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        BaseErrorCode errorCode = GlobalErrorCode.METHOD_NOT_SUPPORTED;
        ApiResponse<Void> apiExceptionResponse = generateExceptionResponse(errorCode);
        String errorMessage = "Phương thức không được hỗ trợ cho yêu cầu này. " + "Phương thức hiện tại: " + ex.getMethod() + ". " +
                "Các phương thức hỗ trợ: " + ex.getSupportedHttpMethods();
        apiExceptionResponse.setMessage(errorMessage);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiExceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        BaseErrorCode errorCode = GlobalErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<Void> apiExceptionResponse = generateExceptionResponse(errorCode);
        apiExceptionResponse.setMessage("Đã xảy ra lỗi không xác định: " + ex.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiExceptionResponse);
    }

}
