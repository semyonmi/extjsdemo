package ru.semyonmi.extjsdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class EDAuthException extends AuthenticationException {

    private HttpStatus status;

    public EDAuthException(HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
