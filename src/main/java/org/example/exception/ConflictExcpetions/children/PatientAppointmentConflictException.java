package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class PatientAppointmentConflictException extends ConflictException {
    public PatientAppointmentConflictException(String message) {
        super(message);
    }
}
