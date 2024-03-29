package org.example.ws;

import javax.xml.ws.WebFault;
import org.example.errors.ItemServiceFault;

@WebFault(faultBean = "org.example.errors.ItemServiceFault")
public class ServiceException extends Exception {
    private static final long serialVersionUID = -6647544772732631537L;
    private final ItemServiceFault fault;

    public ServiceException(String message, ItemServiceFault fault) {
        super(message);
        this.fault = fault;
    }

    public ServiceException(String message, ItemServiceFault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public ItemServiceFault getFaultInfo() {
        return fault;
    }
}
