package org.example;

import org.example.client.ws.Item;
import org.example.client.ws.SoapItemWebService;
import org.example.client.ws.SoapItemWebService_Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Client {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:42577/ws/ws/itemService?wsdl");
        SoapItemWebService_Service itemService = new SoapItemWebService_Service(url);
        List<Item> items = itemService.getSoapItemWebServicePort().getItems();

        for (Item item :items) {
            System.out.println("_____________________");
            System.out.println(item.getName());
            System.out.println(item.getDescription());
            System.out.println(item.getPrice());
            System.out.println(item.getLevel());
            System.out.println(item.getPower());
        }

    }
}
