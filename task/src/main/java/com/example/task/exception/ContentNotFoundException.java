package com.example.task.exception;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException() {
        super();
    }

    public ContentNotFoundException(String message) {
        super(message);
    }
}
