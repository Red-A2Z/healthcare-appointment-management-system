package org.example.exception;

public class PatientAppointmentConflictException extends RuntimeException {
    public PatientAppointmentConflictException(String message) {
        super(message);
    }
}
