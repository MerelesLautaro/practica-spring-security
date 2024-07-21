package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Role;
import com.lautadev.practica_spring_security.model.UserSec;
import com.lautadev.practica_spring_security.repository.IUserSecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserSecService implements IUserSecService{
    @Autowired
    private IUserSecRepository userSecRepository;

    @Autowired
    private IRoleService roleService;

    @Override
    public void saveUser(UserSec userSec) {
        Set<Role> roleList = new HashSet<>();

        for(Role role: userSec.getRoleList()){
            Role readRole = roleService.findRole(role.getId()).orElse(null);
            if(readRole!=null){
                roleList.add(readRole);
            }
        }

        if(!roleList.isEmpty()){
            userSec.setRoleList(roleList);
            userSecRepository.save(userSec);
        }
    }

    @Override
    public List<UserSec> getUsers() {
        return userSecRepository.findAll();
    }

    @Override
    public Optional<UserSec> findUser(Long id) {
        return userSecRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userSecRepository.deleteById(id);
    }

    @Override
    public void editUser(UserSec userSec) {
        this.saveUser(userSec);
    }
}
