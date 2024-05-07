package com.rifl.app.user.services;

import com.rifl.app.user.entities.User;
import com.rifl.app.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getDisabledUsers() {
        return userRepository.findByEnabledFalse(false);
    }

}
