package com.chatop.chatopback.repositories;

import com.chatop.chatopback.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM users u WHERE u.email= :email and u.password= :password")
    Optional<User> findUserByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );
}
