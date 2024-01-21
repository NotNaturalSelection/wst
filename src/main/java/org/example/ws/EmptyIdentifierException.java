package org.example.ws;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyIdentifierException extends RuntimeException {
    private static final long serialVersionUID = -6647544772732631047L;

    public EmptyIdentifierException(String message) {
        super(message);
    }

    public EmptyIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

}
