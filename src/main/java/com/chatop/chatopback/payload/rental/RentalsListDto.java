package com.chatop.chatopback.payload.rental;

import lombok.Data;

import java.util.List;

@Data
public class RentalsListDto {
    private List<RentalDto> rentals;

    public RentalsListDto(List<RentalDto> rentals) {
        this.rentals = rentals;
    }
}
