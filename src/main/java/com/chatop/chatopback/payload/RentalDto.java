package com.chatop.chatopback.payload;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RentalDto {
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;
}
