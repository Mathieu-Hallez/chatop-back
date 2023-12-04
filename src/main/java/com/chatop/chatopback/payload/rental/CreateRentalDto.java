package com.chatop.chatopback.payload.rental;

import lombok.Data;

@Data
public class CreateRentalDto extends UpdateRentalDto{
    private String picture;
}
