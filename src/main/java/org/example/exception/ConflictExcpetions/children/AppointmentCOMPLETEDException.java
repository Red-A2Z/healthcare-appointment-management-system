package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class AppointmentCOMPLETEDException extends ConflictException {
    public AppointmentCOMPLETEDException(String message) {
        super(message);
    }
}
