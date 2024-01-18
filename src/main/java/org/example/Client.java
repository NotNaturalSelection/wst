package org.example;

import org.example.client.ws.Item;
import org.example.client.ws.SoapItemWebService_Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Client {

    private static String str(Item item) {
        return "Item{" +
                "name='" + item.getName() + '\'' +
                ", description='" + item.getDescription() + '\'' +
                ", price=" + item.getPrice() +
                ", level=" + item.getLevel() +
                ", power=" + item.getPower() +
                '}';
    }
    private static final String NEW_ITEM_NAME = "Magic wand";
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/itemService?wsdl");
        SoapItemWebService_Service itemService = new SoapItemWebService_Service(url);
        List<Item> items = itemService.getSoapItemWebServicePort().getItems();
        System.out.println("Items count: " + items.size());
        System.out.println("Creating item...");

        itemService.getSoapItemWebServicePort().createItem(NEW_ITEM_NAME, "The simplest magic accessories.", 15, 15, 5);

        items = itemService.getSoapItemWebServicePort().getItems();
        System.out.println("Items count: " + items.size());
        for (Item item : items) {
            System.out.println(str(item));
        }

        itemService.getSoapItemWebServicePort().updateItem(NEW_ITEM_NAME, "The simplest magic accessories. New part of description will arrive soon", 15, 100, 5);

        items = itemService.getSoapItemWebServicePort().getItems();
        System.out.println("Items count: " + items.size());
        for (Item item : items) {
            System.out.println(str(item));
        }

        itemService.getSoapItemWebServicePort().deleteItem(NEW_ITEM_NAME);
        System.out.println("Removing element...");
        items = itemService.getSoapItemWebServicePort().getItems();
        System.out.println("Items count: " + items.size());
    }
}
