package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserSecService {
    public void saveUser(UserSec userSec);
    public List<UserSec> getUsers();
    public Optional<UserSec> findUser(Long id);
    public void deleteUser(Long id);
    public void editUser(UserSec userSec);
}
