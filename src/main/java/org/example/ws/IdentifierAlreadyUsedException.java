package org.example.ws;

//@ResponseStatus(value = HttpStatus.CONFLICT)

public class IdentifierAlreadyUsedException extends RuntimeException {
    private static final long serialVersionUID = -6647544772732631537L;

    public IdentifierAlreadyUsedException(String message) {
        super(message);
    }

    public IdentifierAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

}
