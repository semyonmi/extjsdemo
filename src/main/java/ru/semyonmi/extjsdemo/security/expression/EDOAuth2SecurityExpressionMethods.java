package ru.semyonmi.extjsdemo.security.expression;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods;

import java.util.Optional;

import ru.semyonmi.extjsdemo.domain.Access;
import ru.semyonmi.extjsdemo.domain.Role;
import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.util.OAuthUtils;

public class EDOAuth2SecurityExpressionMethods extends OAuth2SecurityExpressionMethods {

    private final ApplicationContext appContext;

    public EDOAuth2SecurityExpressionMethods(ApplicationContext appContext, Authentication authentication) {
        super(authentication);
        this.appContext = appContext;
    }

    public boolean isAdmin() {
        User user = getUser();
        Optional<Role> roleOptional = Optional.of(user.getRole());
        return StringUtils.equals("ADMIN", roleOptional.get().getIdent());
    }

    public boolean hasReadAccess() {
        return getAccess().canRead();
    }

    public boolean hasWriteAccess() {
        return getAccess().canWrite();
    }

    private Access getAccess() {
        Optional<Access> accessOptional = Optional.ofNullable(getUser().getAccess());
        accessOptional.orElseThrow(() -> new AccessDeniedException(""));
        return accessOptional.get();
    }

    private User getUser() {
        Optional<User> userOptional = Optional.ofNullable(getOAuthUtils().getUser());
        User user = userOptional.orElseThrow(() -> new BadCredentialsException("Not auth"));
        return user;
    }

    private OAuthUtils getOAuthUtils() {
        return appContext.getBean(OAuthUtils.class);
    }
}
