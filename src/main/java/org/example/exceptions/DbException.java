package org.example.exceptions;

public class DbException extends RuntimeException {
    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}
