package com.chatop.chatopback.payload.message;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageRequestDto {
    private String message;
    @JsonAlias("user_id")
    @JsonProperty("user_id")
    private Long userId;
    @JsonAlias("rental_id")
    @JsonProperty("rental_id")
    private Long rentalId;
}
