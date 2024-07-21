package com.lautadev.practica_spring_security.repository;

import com.lautadev.practica_spring_security.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserSecRepository extends JpaRepository<UserSec,Long> {
}
