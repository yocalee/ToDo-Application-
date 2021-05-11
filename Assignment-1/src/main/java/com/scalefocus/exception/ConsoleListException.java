package com.scalefocus.exception;

public class ConsoleListException extends RuntimeException{

    public ConsoleListException(String message) {
        super(message);
    }

    public ConsoleListException(String message, Throwable cause) {
        super(message, cause);
    }
}
