package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    public void savePermission(Permission permission);
    public List<Permission> getPermissions();
    public Optional<Permission> findPermission(Long id);
    public void deletePermission(Long id);
    public void editPermission(Permission permission);
}
