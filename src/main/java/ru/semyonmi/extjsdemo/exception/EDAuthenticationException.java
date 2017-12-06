package ru.semyonmi.extjsdemo.exception;

public class EDAuthenticationException extends RuntimeException {

    public EDAuthenticationException(String msg) {
        super(msg);
    }

    public EDAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
