package org.example.ws;

//@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ResourceForbiddenException extends RuntimeException {
    public ResourceForbiddenException() {
    }

    public ResourceForbiddenException(String message) {
        super(message);
    }
}
