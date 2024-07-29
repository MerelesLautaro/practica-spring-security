package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Account;
import com.lautadev.practica_spring_security.model.GoogleUserInfo;
import com.lautadev.practica_spring_security.model.UserSec;
import com.lautadev.practica_spring_security.repository.IAccountRepository;
import com.lautadev.practica_spring_security.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {
    @Autowired
    private IUserSecService userSecService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            OidcUser processedUser = processOidcUser(userRequest, oidcUser);
            setAuthentication(processedUser);
            String jwtToken = createAndSetJwtToken(processedUser);
            addJwtTokenToResponse(jwtToken);

            // Imprimir el token en la consola
            System.out.println("Generated JWT Token: " + jwtToken);

            return processedUser;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());

        Optional<Account> userOptional = accountRepository.findByEmail(googleUserInfo.getEmail());
        if (!userOptional.isPresent()) {
            UserSec userSec = userSecService.saveUserOAuth(googleUserInfo);

            if(userSec.getId()!=null) {
                Account account = new Account();
                account.setEmail(googleUserInfo.getEmail());
                account.setName(googleUserInfo.getName());
                account.setLastname(googleUserInfo.getLastname());
                account.setUserSec(userSec);

                accountService.saveAccount(account);
            }
        }

        return oidcUser;
    }

    private void setAuthentication(OidcUser oidcUser) {
        Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
        Authentication authentication = new UsernamePasswordAuthenticationToken(oidcUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String createAndSetJwtToken(OidcUser oidcUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return jwtUtils.createToken(authentication);
    }

    private void addJwtTokenToResponse(String jwtToken) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        }
    }

}
