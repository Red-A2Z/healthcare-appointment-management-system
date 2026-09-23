package org.example.exception;

public class DoctorInactiveException extends RuntimeException {
    public DoctorInactiveException(String message) {
        super(message);
    }
}
