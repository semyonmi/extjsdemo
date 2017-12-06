package ru.semyonmi.extjsdemo.exception;

import org.springframework.http.HttpStatus;

public class LockedException extends EDAuthException {

    public LockedException(String message) {
        super(HttpStatus.LOCKED, message);
    }
}
