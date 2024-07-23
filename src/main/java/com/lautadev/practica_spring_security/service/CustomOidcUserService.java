package com.lautadev.practica_spring_security.service;

import com.lautadev.practica_spring_security.model.Account;
import com.lautadev.practica_spring_security.model.GoogleUserInfo;
import com.lautadev.practica_spring_security.model.UserSec;
import com.lautadev.practica_spring_security.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

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

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
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

}
