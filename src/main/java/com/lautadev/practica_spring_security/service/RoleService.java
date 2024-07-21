package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Permission;
import com.lautadev.practica_spring_security.model.Role;
import com.lautadev.practica_spring_security.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService implements IRoleService{
    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionService permissionService;

    @Override
    public void saveRole(Role role) {
        Set<Permission> permissionsList = new HashSet<>();

        for(Permission permission: role.getPermissionsList()){
            Permission readPermission = permissionService.findPermission(permission.getId()).orElse(null);
            if(readPermission!=null){
                permissionsList.add(readPermission);
            }
        }

        role.setPermissionsList(permissionsList);
        roleRepository.save(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findRole(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void editRole(Role role) {
        this.saveRole(role);
    }
}
