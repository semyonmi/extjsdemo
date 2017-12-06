package ru.semyonmi.extjsdemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.exception.EDAuthenticationException;
import ru.semyonmi.extjsdemo.security.OAuth2Security;

@Component
public class OAuthUtils {

    @Autowired
    OAuth2Security oauth2Security;

    public User getUser() throws EDAuthenticationException {
        return oauth2Security.getUser(getToken());
    }
    public String getToken() throws EDAuthenticationException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null &&
                context.getAuthentication() != null &&
                context.getAuthentication().getDetails() instanceof OAuth2AuthenticationDetails) {
            return ((OAuth2AuthenticationDetails)context.getAuthentication().getDetails()).getTokenValue();
        }
        throw new EDAuthenticationException("User is not authenticated");
    }

    public void removeToken() {
        oauth2Security.revokeToken(getToken());
    }
}
