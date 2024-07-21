package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    public void saveRole(Role role);
    public List<Role> getRoles();
    public Optional<Role> findRole(Long id);
    public void deleteRole(Long id);
    public void editRole(Role role);
}
