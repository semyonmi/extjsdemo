package ru.semyonmi.extjsdemo.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.semyonmi.extjsdemo.exception.EDAuthException;

public class EDAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Security Application\"");
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        if (authException instanceof EDAuthException) {
            status = ((EDAuthException)authException).getStatus().value();
        }
        response.sendError(status, authException.getMessage());
    }
}
