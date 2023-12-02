package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.payload.authentication.AuthResponseDto;
import com.chatop.chatopback.payload.authentication.LoginRequestDto;
import com.chatop.chatopback.payload.authentication.RegisterRequestDto;
import com.chatop.chatopback.payload.authentication.UserDto;
import com.chatop.chatopback.service.JWTService;
import com.chatop.chatopback.services.UserService;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        Optional<DBUser> dbUser = this.userService.getUser(authentication.getName());
        if(dbUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(modelMapper.map(dbUser.get(), UserDto.class), HttpStatus.FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> getToken(@RequestBody LoginRequestDto userLogin) throws IllegalAccessException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getLogin(), userLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        log.info("Token requested for user :{}", authentication.getAuthorities());
        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto userRegister) throws IllegalAccessException {
        Optional<DBUser> userOptional = this.userService.getUser(userRegister.getEmail());
        if(userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        DBUser dbUser = new DBUser();
        dbUser.setEmail(userRegister.getEmail());
        dbUser.setName(userRegister.getName());
        dbUser.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        dbUser.setCreatedAt(Timestamp.from(Instant.now()));
        dbUser.setUpdatedAt(Timestamp.from(Instant.now()));

        DBUser dbUserSaved = userService.saveUser(dbUser);

        log.info("User Saved: " + dbUserSaved.getEmail() + " with id: " + dbUserSaved.getId());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dbUserSaved.getEmail(), userRegister.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);

        return new ResponseEntity<AuthResponseDto>(new AuthResponseDto(token), HttpStatus.CREATED);
    }
}
