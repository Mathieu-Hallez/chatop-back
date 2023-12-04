package com.chatop.chatopback.payload.rental;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateRentalDto {
    private String name;
    private BigDecimal price;
    private BigDecimal surface;
    private String description;
}
