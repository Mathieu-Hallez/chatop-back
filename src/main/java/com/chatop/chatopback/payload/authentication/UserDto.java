package com.chatop.chatopback.payload.authentication;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
