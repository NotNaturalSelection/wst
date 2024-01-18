package org.example;

import org.example.common.Item;
import org.example.ws.EmptyIdentifierException;
import org.example.ws.IdentifierAlreadyUsedException;
import org.example.ws.ItemNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

public class Client {
    RestClient client;

    public Client(RestClient client) {
        this.client = client;
    }

    public List<Item> getItems() {
        return client.get().retrieve().toEntity(new ParameterizedTypeReference<List<Item>>() {}).getBody();
    }

    public String createItem(Item item) {
        try {
            return client.post().body(item, new ParameterizedTypeReference<>() {
            }).retrieve().body(String.class);

        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode().value()) {
                case 409:
                    throw new IdentifierAlreadyUsedException(e.getMessage(), e);
                case 400:
                    throw new EmptyIdentifierException(e.getMessage(), e);
                default:
                    throw e;
            }
        }
    }

    public void updateItem(Item item) throws ItemNotFoundException {
        try {
            client.put().body(item, new ParameterizedTypeReference<>() {
            }).retrieve().body(String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(404)) {
                throw new ItemNotFoundException(e.getMessage(), e);
            } else {
                throw e;
            }
        }
    }

    public String deleteItem(String name) throws ItemNotFoundException {
        try {
            return client.delete().uri("http://localhost:8080/items?name="+name).retrieve().body(String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(404)) {
                throw new ItemNotFoundException(e.getMessage(), e);
            } else {
                throw e;
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client(RestClient.create("http://localhost:8080/items"));
        System.out.println("Listing elements...");
        System.out.println(client.getItems());
        System.out.println("Expecting error creating item with empty name...");
        try {
            client.createItem(new Item("", "", 100, 100, 100));
        } catch (EmptyIdentifierException e) {
            System.err.println("ERROR: identifier is not filled!");
        }
        System.out.println("Expecting error creating new item with conflicting name...");
        try {
            client.createItem(new Item("Something expensive", "Proper description", 100, 100, 100));
        } catch (IdentifierAlreadyUsedException e) {
            System.err.println("ERROR: identifier is already in use!");
        }
        System.out.println("Expecting error updating item with wrong name...");
        try {
            client.updateItem(new Item("WRONG NAME", "", 10000, 100, 100));
        } catch (ItemNotFoundException e) {
            System.err.println("ERROR: item with such identifier is not found!");
        }
        System.out.println("Expecting error deleting item with wrong name...");
        try {
            client.deleteItem("WRONG NAME");
        } catch (ItemNotFoundException e) {
            System.err.println("ERROR: item with such identifier is not found!");
        }
        System.out.println(client.getItems());
    }
}
