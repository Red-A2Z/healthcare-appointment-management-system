package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class DoctorUnavailableException extends ConflictException {
    public DoctorUnavailableException(String message) {
        super(message);
    }
}
