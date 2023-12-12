package com.chatop.chatopback.repositories;

import com.chatop.chatopback.model.DBUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<DBUser, Long> {
    Optional<DBUser> findUserByEmail(
            @Param("email") String email
    );
    Optional<DBUser> findById(Long aLong);
}
