package ru.semyonmi.extjsdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ru.semyonmi.extjsdemo.dto.JsonException;
import ru.semyonmi.extjsdemo.exception.BadRequestException;

@ControllerAdvice(annotations = RestController.class)
public class GlobalControllerExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class, BadRequestException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private ResponseEntity<?> errorHandler(IllegalArgumentException e) {
        logger.info("", e);
        return new ResponseException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class, HttpServerErrorException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private ResponseEntity<?> errorHandler(HttpStatusCodeException e) {
        String bodyAsString = e.getResponseBodyAsString();
        logger.info(bodyAsString, e);
        return new ResponseEntity<>(new JsonException(bodyAsString), e.getStatusCode());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    private ResponseEntity<?> errorHandler(AccessDeniedException e) {
        String bodyAsString = e.getMessage();
        logger.info(bodyAsString, e);
        return new ResponseEntity<>(new JsonException(bodyAsString), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<?> errorHandler(NullPointerException e) {
        logger.error("", e);
        return new ResponseException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<?> errorHandler(Throwable e) {
        logger.error("", e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e.getCause() == null) {
            return new ResponseException(e, status);
        }
        if (e.getCause().getCause() == null) {
            return new ResponseException(e.getCause(), status);
        }
        return new ResponseException(e.getCause().getCause(), status);
    }

    private static class ResponseException extends ResponseEntity<JsonException> {

        public ResponseException(Throwable e, HttpStatus statusCode) {
            super(new JsonException(e), statusCode);
        }
    }
}
