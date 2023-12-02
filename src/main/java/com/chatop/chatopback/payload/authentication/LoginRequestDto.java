package com.chatop.chatopback.payload.authentication;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String login;
    private String password;
}
