package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.DBUser;
import com.chatop.chatopback.service.JWTService;
import com.chatop.chatopback.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/user")
    public DBUser getUser(@RequestParam("email") final String email) {
        Optional<DBUser> user = this.userService.getUser(email);
        return user.orElse(null);
    }

    @PostMapping("/login")
    public String getToken(Authentication authentication) {
        log.info("Token requested for user :{}", authentication.getAuthorities());
        String token = jwtService.generateToken(authentication);

        return token;
    }
}
