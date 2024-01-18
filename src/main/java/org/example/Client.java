package org.example;

import org.example.common.Item;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
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
        return client.post().body(item, new ParameterizedTypeReference<>() {
        }).retrieve().body(String.class);
    }

    public void updateItem(Item item) {
        client.put().body(item, new ParameterizedTypeReference<>() {}).retrieve();
    }

    public String deleteItem(String name) {
        return client.delete().uri("http://localhost:8080/items?name="+name).retrieve().body(String.class);
    }

    public static void main(String[] args) {
        Client client = new Client(RestClient.create("http://localhost:8080/items"));
        System.out.println("Listing elements...");
        System.out.println(client.getItems());
        client.createItem(new Item("Something expensive", "Proper description", 100, 100, 100));
        client.updateItem(new Item("Something expensive", "Make it more expensive", 10000, 100, 100));
        System.out.println("Checking that element is added...");
        System.out.println(client.getItems());
        client.deleteItem("Something expensive");
        System.out.println("Checking that element is removed...");
        System.out.println(client.getItems());
    }
}
