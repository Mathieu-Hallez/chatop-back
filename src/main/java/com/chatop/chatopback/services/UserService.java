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

    public Optional<DBUser> getUser(Long id) {
        return this.userRepository.findById(id);
    }

    /**
     * Register a new user if the email is not used.
     * @param user DBUser
     * @return Optional DBUser
     */
    public Optional<DBUser> registerUser(DBUser user) {
        // Test unique user in DB
        Optional<DBUser> userOptional = this.userRepository.findUserByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(this.userRepository.save(user));
    }
}
