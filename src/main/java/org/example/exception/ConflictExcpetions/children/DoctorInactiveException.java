package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class DoctorInactiveException extends ConflictException {
    public DoctorInactiveException(String message) {
        super(message);
    }
}
