package org.example.exception.ConflictExcpetions.children.AppointmentStatusConflictExceptions;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class AppointmentStatusConflictException extends ConflictException {
    public AppointmentStatusConflictException(String message) {
        super(message);
    }
}
