package com.rifl.app.user.repositories;

import com.rifl.app.user.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User>  findByEmail(String email);
    Optional<User>  findByIdentifier(String identifier);

}