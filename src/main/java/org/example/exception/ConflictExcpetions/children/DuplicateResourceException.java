package org.example.exception.ConflictExcpetions.children;

import org.example.exception.ConflictExcpetions.parent.ConflictException;

public class DuplicateResourceException extends ConflictException {

    public DuplicateResourceException(String message){
        super(message);
    }

}
