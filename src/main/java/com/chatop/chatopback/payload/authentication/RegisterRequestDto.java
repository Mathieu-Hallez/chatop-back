package com.chatop.chatopback.payload.authentication;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String name;
    private String password;
}
