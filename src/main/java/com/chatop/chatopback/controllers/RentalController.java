package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.model.Rental;
import com.chatop.chatopback.payload.rental.CreateRentalDto;
import com.chatop.chatopback.payload.rental.RentalDto;
import com.chatop.chatopback.payload.rental.RentalsListDto;
import com.chatop.chatopback.payload.rental.UpdateRentalDto;
import com.chatop.chatopback.services.RentalService;
import com.chatop.chatopback.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "The Rental API. Contains all the operations that can be performed on a rental.")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Read - Get one rental.
     * @param id - The id of the rental.
     * @return - A Rental object fulfilled.
     */
    @Operation(
            summary = "Get a rental.",
            description="Get a rental by its identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A rental.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RentalDto.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())}),
                    @ApiResponse(responseCode = "404", description = "Rental not found in DB.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRental(@PathVariable("id") final Long id) {
        Optional<Rental> rental = this.rentalService.getRental(id);
        if(rental.isEmpty()) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(modelMapper.map(rental.get(), RentalDto.class), HttpStatus.OK);
    }

    /**
     * Read - Get all rentals.
     * @return - A list object of Rentals fulfilled.
     */
    @Operation(
            summary = "Get all rentals.",
            description = "Get all rentals.",
            responses = {
                @ApiResponse(responseCode = "200", description = "A list of rental.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RentalsListDto.class)) }),
                @ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @GetMapping("")
    public ResponseEntity<RentalsListDto> getRentals() {
        List<RentalDto> listRentals = new ArrayList<>();
        Iterable<Rental> rentals = this.rentalService.getRentals();
        rentals.forEach(rental -> listRentals.add(modelMapper.map(rental, RentalDto.class)));
        return new ResponseEntity<RentalsListDto>(new RentalsListDto(listRentals),HttpStatus.OK);
    }

    /**
     * Update - Edit a Rental.
     * @param id - The id of the Rental to edit.
     * @param rental - New Rental data.
     * @return - A Rental object fulfilled.
     */
    @Operation(
            summary = "Update a rental.",
            description="Update a rental by passing is identifier in the URI.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A message that indicate the success of update.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = com.chatop.chatopback.payload.api.ApiResponse.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())}),
                    @ApiResponse(responseCode = "404", description = "Rental to update not found in DB.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<com.chatop.chatopback.payload.api.ApiResponse> updateRental(@PathVariable("id") final Long id, @ModelAttribute UpdateRentalDto rental) {
        com.chatop.chatopback.payload.api.ApiResponse apiResponse = new com.chatop.chatopback.payload.api.ApiResponse("Rental Updated !");

        Optional<Rental> r = this.rentalService.getRental(id);
        if(r.isEmpty()) {
            apiResponse.setMessage("Rental with id " + id + " doesn't find.");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        Rental currentRental = r.get();
        if(rental.getName() != null)
            currentRental.setName(rental.getName());
        if(rental.getSurface() != null )
            currentRental.setSurface(rental.getSurface());
        if(rental.getPrice() != null)
            currentRental.setPrice(rental.getPrice());
        if( rental.getDescription() != null)
            currentRental.setDescription(rental.getDescription());
        currentRental.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        this.rentalService.saveRental(currentRental);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Create - Create a new rental.
     * @param rentalDto - The rental to populate.
     * @return - A ApiResponse with a message.
     */
    @Operation(
            summary = "Create a new rental.",
            description="Create a new rental",
            responses = {
                    @ApiResponse(responseCode = "201", description = "A message that indicate the success of creation.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = com.chatop.chatopback.payload.api.ApiResponse.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<com.chatop.chatopback.payload.api.ApiResponse> createRental(Authentication authentication, @ModelAttribute final CreateRentalDto rentalDto) {
        com.chatop.chatopback.payload.api.ApiResponse apiResponse = new com.chatop.chatopback.payload.api.ApiResponse("Rental created!");
        try {
            Rental rental = modelMapper.map(rentalDto, Rental.class);
            Timestamp now = Timestamp.from(Instant.now());
            rental.setPicture(rentalDto.getPicture().getOriginalFilename());
            rental.setCreatedAt(now);
            rental.setUpdatedAt(now);

            Optional<DBUser> dbUser = this.userService.getUser(authentication.getName());
            if(dbUser.isEmpty()) {
                apiResponse.setMessage("Not authenticate.");
                return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            }
            rental.setOwner(dbUser.get());

            this.rentalService.saveRental(rental);
        } catch(Exception ex) {
            apiResponse.setMessage(ex.getMessage());
        }

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
