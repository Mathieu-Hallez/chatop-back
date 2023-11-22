package com.chatop.chatopback.controllers;

import com.chatop.chatopback.model.User;
import com.chatop.chatopback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public User getUser(@PathVariable("email") final String email, @PathVariable("password") final String password) {
        Optional<User> user = this.userService.getUser(email, password);
        return user.orElse(null);
    }
}
