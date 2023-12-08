package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.payload.authentication.UserDto;
import com.chatop.chatopback.payload.rental.RentalDto;
import com.chatop.chatopback.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.websocket.server.PathParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    public UserService userService;
    @Autowired
    public ModelMapper modelMapper;

    /**
     * GET - Get a user by id
     * @param id - User identifier
     * @return UserDto founded
     */
    @Operation(
            summary = "Get a user.",
            description="Get a user by an identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A user.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())}),
                    @ApiResponse(responseCode = "404", description = "User not found in DB.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getAUser(@PathVariable("id") final Long id) {
        Optional<DBUser> userOptional = this.userService.getUser(id);
        if(userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modelMapper.map(userOptional.get(), UserDto.class), HttpStatus.OK);
    }
}
