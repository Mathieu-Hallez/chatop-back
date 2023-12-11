package com.chatop.chatopback.payload.authentication;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
