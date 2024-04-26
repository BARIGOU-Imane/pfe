package com.rifl.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @GetMapping("/hello")
    @PreAuthorize("hasRole('client_user')")
    public String helloFromAnyUser(){
        return "hello from USER Workspace";
    }

    @GetMapping("/helloAdmin")
    @PreAuthorize("hasRole('client_admin')")
    public String helloFromAdmin(){
        return "hello from ADMIN Workspace";
    }

    @GetMapping("/helloSuper")
    @PreAuthorize("hasRole('client_super')")
    public String helloFromSuper(){
        return "hello from SUPERVISEUR Workspace";
    }


}
