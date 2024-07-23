package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.GoogleUserInfo;
import com.lautadev.practica_spring_security.model.Role;
import com.lautadev.practica_spring_security.model.UserSec;
import com.lautadev.practica_spring_security.repository.IUserSecRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserSecService implements IUserSecService{
    @Autowired
    private IUserSecRepository userSecRepository;

    @Autowired
    private IRoleService roleService;

    @Override
    public void saveUser(UserSec userSec) {
        Set<Role> roleList = new HashSet<>();

        userSec.setPassword(this.encriptPassword(userSec.getPassword()));

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
        userSecRepository.save(userSec);
    }

    @Override
    public UserSec saveUserOAuth(GoogleUserInfo googleUserInfo) {
        UserSec userSec = new UserSec();
        userSec.setUsername(googleUserInfo.getEmail());
        String randomPassword = RandomStringUtils.randomAlphanumeric(12);
        userSec.setPassword(randomPassword);
        userSec.setEnabled(true);
        userSec.setAccountNotLocked(true);
        userSec.setAccountNotExpired(true);
        userSec.setCredentialNotExpired(true);
        Set<Role> roleList = roleService.findRoleByName("USER");
        userSec.setRoleList(roleList);
        this.saveUser(userSec);
        return userSec;
    }

    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
