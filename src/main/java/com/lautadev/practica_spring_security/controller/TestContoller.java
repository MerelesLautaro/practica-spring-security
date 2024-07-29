package com.lautadev.practica_spring_security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
public class TestContoller {

    @GetMapping("/testing")
    public String hola(){
        return "Hola papu";
    }
}
