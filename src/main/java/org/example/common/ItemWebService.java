package org.example.common;

import jakarta.jws.WebParam;
import org.example.ws.EmptyIdentifierException;
import org.example.ws.IdentifierAlreadyUsedException;
import org.example.ws.ItemNotFoundException;
import org.example.ws.ServiceException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ItemWebService {
    List<Item> getItems() throws ServiceException;

    Item getItem(String name) throws ItemNotFoundException, ServiceException, EmptyIdentifierException;
    String createItem(String name,  String description, int level, int power, int price) throws ServiceException, IdentifierAlreadyUsedException, EmptyIdentifierException, IOException;
    void updateItem(String name,  String description, int level, int power, int price) throws ItemNotFoundException, EmptyIdentifierException;
    void deleteItem(String name) throws ItemNotFoundException, EmptyIdentifierException;
}
