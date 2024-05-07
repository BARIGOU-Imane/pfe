package com.rifl.app.user.repositories;

import com.rifl.app.user.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User>  findByEmail(String email);

    Optional<User>  findByIdentifier(String identifier);

    @Query("SELECT u FROM User u WHERE u.enabled = :enabled")
    List<User> findByEnabledFalse(@Param("enabled") Boolean enabled);


}