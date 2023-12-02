package com.chatop.chatopback.services;

import com.chatop.chatopback.model.DBUser;
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

    public Optional<DBUser> getUser(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    public DBUser saveUser(DBUser user) {
        return this.userRepository.save(user);
    }
}
