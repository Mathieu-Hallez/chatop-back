package com.chatop.chatopback.payload.error;

import lombok.Data;

@Data
public class ApiErrorResponse {
    private String message;

    public ApiErrorResponse(String msg) {
        this.message = msg;
    }
}
