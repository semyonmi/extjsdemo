package ru.semyonmi.extjsdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.exception.EDAuthenticationException;

@Component
public class OAuth2Security {

    @Autowired
    private ConsumerTokenServices tokenServices;

    @Autowired
    private TokenStore tokenStore;

    public User getUser(String token) throws EDAuthenticationException {
        return getCGUserDetails(token).getUser();
    }

    public void revokeToken(String token) {
        tokenServices.revokeToken(token);
    }

    private EDUserDetails getCGUserDetails(String token) throws EDAuthenticationException {
        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        if (!(authentication.getPrincipal() instanceof EDUserDetails)) {
            throw new EDAuthenticationException("Authentication is not supported");
        }
        return (EDUserDetails) authentication.getPrincipal();
    }
}
