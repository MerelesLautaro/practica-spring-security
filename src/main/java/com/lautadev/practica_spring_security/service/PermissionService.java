package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Permission;
import com.lautadev.practica_spring_security.repository.IPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService{
    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findPermission(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public void editPermission(Permission permission) {
        permissionRepository.save(permission);
    }
}
