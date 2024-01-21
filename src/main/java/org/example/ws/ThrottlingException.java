package org.example.ws;

//@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class ThrottlingException extends RuntimeException {

    private static final long serialVersionUID = -6647544772732631027L;

    public ThrottlingException(String message) {
        super(message);
    }

    public ThrottlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
