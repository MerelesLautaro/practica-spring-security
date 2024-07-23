package com.lautadev.practica_spring_security.repository;

import com.lautadev.practica_spring_security.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
}
