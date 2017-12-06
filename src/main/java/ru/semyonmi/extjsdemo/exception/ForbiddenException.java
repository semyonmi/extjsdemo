package ru.semyonmi.extjsdemo.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends EDAuthException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
