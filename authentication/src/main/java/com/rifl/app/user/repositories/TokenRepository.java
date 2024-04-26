package com.rifl.app.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rifl.app.user.entities.Token;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
}