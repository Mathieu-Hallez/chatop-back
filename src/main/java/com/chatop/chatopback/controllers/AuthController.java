package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.payload.authentication.AuthResponseDto;
import com.chatop.chatopback.payload.authentication.LoginRequestDto;
import com.chatop.chatopback.service.JWTService;
import com.chatop.chatopback.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user")
    public DBUser getUser(@RequestParam("email") final String email) {
        Optional<DBUser> user = this.userService.getUser(email);
        return user.orElse(null);
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody LoginRequestDto userLogin) throws IllegalAccessException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getLogin(), userLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        log.info("Token requested for user :{}", authentication.getAuthorities());
        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
