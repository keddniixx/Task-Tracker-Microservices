package com.example.task.exception;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException() {
        super("Content not found");
    }

    public ContentNotFoundException(String message) {
        super(message);
    }
}
