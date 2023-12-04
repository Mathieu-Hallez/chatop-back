package com.chatop.chatopback.services;

import com.chatop.chatopback.model.Rental;
import com.chatop.chatopback.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public Optional<Rental> getRental(final Long id) {
        return this.rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals() {
        return this.rentalRepository.findAll();
    }

    public Rental saveRental(Rental rental) {
        return this.rentalRepository.save(rental);
    }
}
