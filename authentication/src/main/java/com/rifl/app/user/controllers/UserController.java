package com.rifl.app.user.controllers;

import com.rifl.app.user.entities.User;
import com.rifl.app.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/disabled")
    public List<User> getDisabledUsers() {
        List<User> users = new ArrayList<>();
        users = userService.getDisabledUsers();
        return users;
    }
}