package org.example.ws;

import jakarta.xml.ws.WebFault;
import org.example.errors.ItemServiceFault;

@WebFault(faultBean = "org.example.errors.ItemServiceFault")
public class EmptyIdentifierException extends Exception {
    private static final long serialVersionUID = -6647544772732631047L;
    private final ItemServiceFault fault;

    public EmptyIdentifierException(String message, ItemServiceFault fault) {
        super(message);
        this.fault = fault;
    }

    public EmptyIdentifierException(String message, ItemServiceFault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public ItemServiceFault getFaultInfo() {
        return fault;
    }
}
