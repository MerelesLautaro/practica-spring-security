package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    public void saveAccount(Account account);
    public List<Account> getAccounts();
    public Optional<Account> findAccount(Long id);
    public void deleteAccount(Long id);
    public void editAccount(Account account);
}
