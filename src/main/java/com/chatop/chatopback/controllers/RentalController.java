package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.Rental;
import com.chatop.chatopback.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
public class RentalController {

    @Autowired
    private RentalService rentalService;

    /**
     * Read - Get one rental
     * @param id - The id of the rental
     * @return - A Rental object fulfilled
     */
    @GetMapping("/rentals/{id}")
    public Rental getRental(@PathVariable("id") final Long id) {
        Optional<Rental> rental = this.rentalService.getRental(id);
        if(rental.isEmpty()) return null;
        return rental.get();
    }

    /**
     * Read - Get all rentals
     * @return - A iterable object of Rental fulfilled
     */
    @GetMapping("/rentals")
    public Iterable<Rental> getRentals() {
        return this.rentalService.getRentals();
    }

    /**
     * Update - Edit a Rental
     * @param id - The id of the Rental to edit
     * @param rental - New Rental data
     * @return - A Rental object fulfilled
     */
    @PutMapping("/rentals/{id}")
    public Rental updateRental(@PathVariable("id") final Long id, @RequestBody Rental rental) {
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
        return currentRental;
    }
}
