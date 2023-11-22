package com.chatop.chatopback.services;

import com.chatop.chatopback.model.User;
import com.chatop.chatopback.repositories.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(String email, String password) {
        return this.userRepository.findUserByEmailAndPassword(email, password);
    }
}
