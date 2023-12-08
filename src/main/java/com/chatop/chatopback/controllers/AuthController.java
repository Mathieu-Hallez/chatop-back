package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.payload.authentication.TokenDto;
import com.chatop.chatopback.payload.authentication.LoginRequestDto;
import com.chatop.chatopback.payload.authentication.RegisterRequestDto;
import com.chatop.chatopback.payload.authentication.UserDto;
import com.chatop.chatopback.payload.api.ApiResponse;
import com.chatop.chatopback.services.JWTService;
import com.chatop.chatopback.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "The Authentication API. Contains all the operations that can be performed for authentication.")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * GET - Get current user login
     * @param authentication The current authentication object
     * @return The user
     */
    @Operation(
            summary = "Get current user login.",
            description="Get current user login.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The current user.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized request.", content = { @Content(mediaType = "application/json", schema = @Schema())}),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rental to update not found in DB.", content = { @Content(mediaType = "application/json", schema = @Schema())})
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        Optional<DBUser> dbUser = this.userService.getUser(authentication.getName());
        if(dbUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(modelMapper.map(dbUser.get(), UserDto.class), HttpStatus.FOUND);
    }

    /**
     * POST - Login to the API.
     * @param userLogin Identification information.
     * @return A new valid token.
     * @throws IllegalAccessException
     */
    @Operation(
            summary = "Login to the API.",
            description="Login to the API and get a valid token.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The current user login.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDto.class))}),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "An error message.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))})
            }
    )
    @PostMapping("/login")
    @SecurityRequirements()
    public ResponseEntity<?> getToken(@RequestBody LoginRequestDto userLogin) throws IllegalAccessException {
        Optional<DBUser> optionalDbUser = this.userService.getUser(userLogin.getLogin());
        if(optionalDbUser.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Unknown user login."), HttpStatus.UNAUTHORIZED);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getLogin(), userLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            log.info("Token requested for user :{}", authentication.getAuthorities());
            String token = jwtService.generateToken(authentication);

            return ResponseEntity.ok(new TokenDto(token));
        }catch(BadCredentialsException ex) {
            return new ResponseEntity<>(new ApiResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * POST - Register to the API and login to receive a valid token.
     * @param userRegister Register information.
     * @return A new valid token.
     * @throws IllegalAccessException
     */
    @Operation(
            summary = "Register to the API.",
            description="Register to the API and get a valid token.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The current user login.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDto.class))}),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "An error message.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))})
            }
    )
    @PostMapping("/register")
    @SecurityRequirements()
    public ResponseEntity<TokenDto> register(@RequestBody RegisterRequestDto userRegister) throws IllegalAccessException {
        DBUser dbUser = new DBUser();
        dbUser.setEmail(userRegister.getEmail());
        dbUser.setName(userRegister.getName());
        dbUser.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        dbUser.setCreatedAt(Timestamp.from(Instant.now()));
        dbUser.setUpdatedAt(Timestamp.from(Instant.now()));

        Optional<DBUser> optionalDBUserSaved = userService.registerUser(dbUser);
        if(optionalDBUserSaved.isEmpty()) {
            log.info("User register failed. User email already used." + dbUser.getEmail());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DBUser dbUserSaved = optionalDBUserSaved.get();
        log.info("User Saved: " + dbUserSaved.getEmail() + " with id: " + dbUserSaved.getId());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dbUserSaved.getEmail(), userRegister.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);

        return new ResponseEntity<TokenDto>(new TokenDto(token), HttpStatus.CREATED);
    }
}
