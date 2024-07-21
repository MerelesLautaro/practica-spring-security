package com.lautadev.practica_spring_security.controller;

import com.lautadev.practica_spring_security.model.UserSec;
import com.lautadev.practica_spring_security.service.IUserSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@PreAuthorize("denyAll()")
public class UserSecController {
    @Autowired
    private IUserSecService userSecService;

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> saveUser(@RequestBody UserSec userSec){
        userSecService.saveUser(userSec);
        return ResponseEntity.ok("User saved succesfully");
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserSec>> getUsers(){
        return ResponseEntity.ok(userSecService.getUsers());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserSec> findUser(@PathVariable Long id){
        Optional<UserSec> userSec = userSecService.findUser(id);
        return userSec.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userSecService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserSec> editUser(@RequestBody UserSec userSec){
        userSecService.editUser(userSec);
        Optional<UserSec> userSecEdit = userSecService.findUser(userSec.getId());
        return userSecEdit.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

}
