package com.banking.app.errors;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(status, error, message, path, LocalDateTime.now(), null);
    }

    public static ApiError withFieldErrors(int status, String error, String message, String path,
                                           List<FieldError> fieldErrors) {
        return new ApiError(status, error, message, path, LocalDateTime.now(), fieldErrors);
    }
}
