package ru.semyonmi.extjsdemo.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class JsonException implements Serializable {

    private static final long serialVersionUID = -2447430240350397944L;

    private String message;

    public JsonException() {
    }

    public JsonException(String message) {
        this.message = message;
    }

    public JsonException(Throwable e) {
        message = StringUtils.isEmpty(e.getMessage()) ? e.toString() : e.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonException that = (JsonException) o;

        if (message != null ? !message.equals(that.message) : that.message != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "JsonException{" +
               "message='" + message + '\'' +
               '}';
    }
}
