package com.chatop.chatopback.payload.rental;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RentalDto extends CreateRentalDto {
    private Long id;
    @JsonAlias("owner_id")
    @JsonProperty("owner_id")
    private Long ownerId;
    @JsonAlias("created_at")
    @JsonProperty("created_at")
    private String createdAt;
    @JsonAlias("updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

}
