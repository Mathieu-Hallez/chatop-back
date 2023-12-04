package com.chatop.chatopback.payload;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RentalDto {
    private Long id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;
    private Long owner_id;
    private String created_at;
    private String updated_at;

}
