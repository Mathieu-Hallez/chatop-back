package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.model.Message;
import com.chatop.chatopback.model.Rental;
import com.chatop.chatopback.payload.api.ApiResponse;
import com.chatop.chatopback.payload.message.MessageRequestDto;
import com.chatop.chatopback.payload.rental.RentalDto;
import com.chatop.chatopback.services.MessageService;
import com.chatop.chatopback.services.RentalService;
import com.chatop.chatopback.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "The Message API. Contains all the operations that can be performed on a message.")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RentalService rentalService;
    @Autowired
    private ModelMapper modelMapper;

    @Operation(
            summary = "Post a new message.",
            description="Post a new message by a user for a specified rental.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success message.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())}),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rental or User not found in DB.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @PostMapping("/")
    public ResponseEntity<ApiResponse> postMessage(@RequestBody @NotNull MessageRequestDto messageRequestDto) {
        ApiResponse apiResponse = new ApiResponse("Message send with success");

        Optional<DBUser> dbUser = this.userService.getUser(messageRequestDto.getUserId());
        if(dbUser.isEmpty()) {
            apiResponse.setMessage("User with id " + messageRequestDto.getUserId() +" doesn't exist.");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
        Optional<Rental> optionalRental = this.rentalService.getRental(messageRequestDto.getRentalId());
        if(optionalRental.isEmpty()) {
            apiResponse.setMessage("Rental with id " + messageRequestDto.getRentalId() +" doesn't exist.");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        Message message = new Message(messageRequestDto.getMessage(), optionalRental.get(), dbUser.get());
        this.messageService.createMessage(message);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
