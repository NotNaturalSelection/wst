package org.example.ws;

import org.example.errors.ItemServiceFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyIdentifierException extends RuntimeException {
    private static final long serialVersionUID = -6647544772732631047L;

    public EmptyIdentifierException(String message) {
        super(message);
    }

    public EmptyIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

}
