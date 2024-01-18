package org.example;

import org.example.client.ws.*;

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
    private static final String NOT_EXISTING_NAME = "ThisNameDoesn'tExist";
    public static void main(String[] args) throws MalformedURLException {
        try {
            URL url = new URL("http://localhost:8080/itemService?wsdl");
            SoapItemWebService_Service itemService = new SoapItemWebService_Service(url);
            List<Item> items = itemService.getSoapItemWebServicePort().getItems();
            System.out.println("Items count: " + items.size());
            for (Item item : items) {
                System.out.println(str(item));
            }
            System.out.println("Creating item with existing identifier and expecting exception...");

            try {
                itemService.getSoapItemWebServicePort().createItem(NEW_ITEM_NAME, "The simplest magic accessories.", 15, 15, 5);
            } catch (IdentifierAlreadyUsedException e) {
                System.err.println("ERROR: Identifier '"+NEW_ITEM_NAME+"' is already used.");
            } catch (EmptyIdentifierException e) {
                System.err.println("ERROR: Identifier 'name' should be provided.");
            }

            System.out.println("Creating item with null identifier and expecting exception...");

            try {
                itemService.getSoapItemWebServicePort().createItem(null, "The simplest magic accessories.", 15, 15, 5);
            } catch (EmptyIdentifierException e) {
                System.err.println("ERROR: Identifier 'name' should be provided.");
            } catch (IdentifierAlreadyUsedException e) {
                System.err.println("ERROR: Identifier '"+NEW_ITEM_NAME+"' is already used.");
            }

            System.out.println("Updating non-existing item and expecting exception...");
            try {
                itemService.getSoapItemWebServicePort().updateItem(NOT_EXISTING_NAME, "The simplest magic accessories. New part of description will arrive soon", 15, 100, 5);
            } catch (ItemNotFoundException e) {
                System.err.println("ERROR: Item is not found by identifier '"+NOT_EXISTING_NAME+"'");
            } catch (EmptyIdentifierException e) {
                System.err.println("ERROR: Identifier 'name' should be provided.");
            }

            System.out.println("Deleting non-existing item and expecting exception...");
            try {
                itemService.getSoapItemWebServicePort().deleteItem(NOT_EXISTING_NAME);
            } catch (ItemNotFoundException e) {
                System.err.println("ERROR: Item is not found by identifier '"+NOT_EXISTING_NAME+"'");
            } catch (EmptyIdentifierException e) {
                System.err.println("ERROR: Identifier 'name' should be provided.");
            }

            items = itemService.getSoapItemWebServicePort().getItems();
            System.out.println("Items count: " + items.size());
            for (Item item : items) {
                System.out.println(str(item));
            }
        } catch (ServiceException e) {
            System.err.println("SHOULD NOT HAPPEN. Something went wrong on the server side: " + e.getFaultInfo().getMessage());
        }
    }
}
