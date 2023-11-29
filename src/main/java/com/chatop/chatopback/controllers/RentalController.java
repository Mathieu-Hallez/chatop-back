package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.Rental;
import com.chatop.chatopback.payload.RentalDto;
import com.chatop.chatopback.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Read - Get one rental
     * @param id - The id of the rental
     * @return - A Rental object fulfilled
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRental(@PathVariable("id") final Long id) {
        Optional<Rental> rental = this.rentalService.getRental(id);
        if(rental.isEmpty()) return null;
        return new ResponseEntity<>(modelMapper.map(rental.get(), RentalDto.class), HttpStatus.FOUND);
    }

    /**
     * Read - Get all rentals
     * @return - A list object of Rentals fulfilled
     */
    @Operation(summary = "Get all rentals")
    @GetMapping("/")
    public List<RentalDto> getRentals() {
        List<RentalDto> listRentals = new ArrayList<>();
        Iterable<Rental> rentals = this.rentalService.getRentals();
        rentals.forEach(rental -> listRentals.add(modelMapper.map(rental, RentalDto.class)));
        return listRentals;
    }

    /**
     * Update - Edit a Rental
     * @param id - The id of the Rental to edit
     * @param rental - New Rental data
     * @return - A Rental object fulfilled
     */
    @PutMapping("/{id}")
    public ResponseEntity<RentalDto> updateRental(@PathVariable("id") final Long id, @RequestBody RentalDto rental) {
        Optional<Rental> r = this.rentalService.getRental(id);
        if(r.isEmpty()) return null;

        Rental currentRental = r.get();
        String name = rental.getName();
        if(name != null) {
            currentRental.setName(name);
        }
        BigDecimal surface = rental.getSurface();
        if(surface != null )
            currentRental.setSurface(surface);
        BigDecimal price = rental.getPrice();
        if(price != null)
            currentRental.setPrice(price);
        String picture = rental.getPicture();
        if(picture != null)
            currentRental.setPicture(picture);
        String description = rental.getDescription();
        if( description != null)
            currentRental.setDescription(description);
        currentRental.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        this.rentalService.saveRental(currentRental);
        return new ResponseEntity<>(modelMapper.map(currentRental, RentalDto.class), HttpStatus.CREATED);
    }
}
