package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class TimePeriodAlreadyCoveredException extends ConflictException {
    public TimePeriodAlreadyCoveredException(String message) {
        super(message);
    }
}
