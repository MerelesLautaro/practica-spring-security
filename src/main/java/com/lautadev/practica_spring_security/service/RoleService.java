package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Permission;
import com.lautadev.practica_spring_security.model.Role;
import com.lautadev.practica_spring_security.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
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

    @Override
    public Set<Role> findRoleByName(String nameRole) {
        List<Role> allRoles = this.getRoles();
        Set<Role> containRole = new HashSet<>();

        for(Role role: allRoles){
            if(Objects.equals(role.getRole(), nameRole)){
                containRole.add(role);
            }
        }

        return containRole;
    }
}
