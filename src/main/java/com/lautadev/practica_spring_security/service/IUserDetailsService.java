package com.lautadev.practica_spring_security.service;


import com.lautadev.practica_spring_security.dto.AuthLoginRequestDTO;
import com.lautadev.practica_spring_security.dto.AuthLoginResponseDTO;
import com.lautadev.practica_spring_security.model.UserSec;
import com.lautadev.practica_spring_security.repository.IUserSecRepository;
import com.lautadev.practica_spring_security.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserSecRepository userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserSec userSec = userRepo.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        //creamos una lista para los permisos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //traer roles y convertirlos en 'SimpleGrantedAuthority'
        userSec.getRoleList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));
        // concatenamos 'ROLE_' para diferenciarlo de los permisos, Spring Security reconoce esto como un ROL


        //traer permisos y convertirlos en 'SimpleGrantedAuthority'
        userSec.getRoleList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermission())));

        return new User(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isAccountNotExpired(),
                userSec.isCredentialNotExpired(),
                authorityList
        );
    }

    public AuthLoginResponseDTO loginUser(AuthLoginRequestDTO userRequest) {
        //recuperar usuario y contraseña
        String username = userRequest.username();
        String password = userRequest.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthLoginResponseDTO(username,"Login Successful", accessToken, true);

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = this.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid username or password");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());

    }
}
