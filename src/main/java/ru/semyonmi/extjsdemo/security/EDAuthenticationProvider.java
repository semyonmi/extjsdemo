package ru.semyonmi.extjsdemo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ru.semyonmi.extjsdemo.config.properties.CoreProperties;
import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.exception.LockedException;
import ru.semyonmi.extjsdemo.service.UserService;

@Component
public class EDAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CoreProperties coreProperties;
    @Autowired
    private UserService userService;

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        try {
            String password = authentication.getCredentials().toString();
            User user = userService.logon(username, password);
            if (user.isLocked())
                throw new LockedException(username);

            return new EDUserDetails(username, password, authentication.getAuthorities(), user);

        } catch (AuthenticationException e) {
            throw e;
        }
        catch (Exception repositoryProblem) {
            logger.error("Authentication failed:", repositoryProblem);
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }
}
