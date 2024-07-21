package com.lautadev.practica_spring_security.controller;

import com.lautadev.practica_spring_security.model.Role;
import com.lautadev.practica_spring_security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRole(@RequestBody Role role) {
        roleService.saveRole(role);
        return ResponseEntity.ok("Role saved Successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<List<Role>> getRoles(){
        return ResponseEntity.ok(roleService.getRoles());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Role> findRole(@PathVariable Long id){
        Optional<Role> role = roleService.findRole(id);
        return role.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted");
    }

    @PutMapping("/edit")
    public ResponseEntity<Role> editRole(@RequestBody Role role){
        roleService.editRole(role);
        Optional<Role> roleEdit = roleService.findRole(role.getId());
        return roleEdit.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
