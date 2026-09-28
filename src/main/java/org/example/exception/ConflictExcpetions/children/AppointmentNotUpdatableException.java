package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class AppointmentNotUpdatableException extends ConflictException {
    public AppointmentNotUpdatableException(String message) {
        super(message);
    }
}
