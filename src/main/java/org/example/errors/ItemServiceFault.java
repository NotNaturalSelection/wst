package org.example.errors;

public class ItemServiceFault {
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ItemServiceFault defaultInstance() {
        ItemServiceFault fault = new ItemServiceFault();
        fault.message = "Item service fault has occurred";
        return fault;
    }
}
