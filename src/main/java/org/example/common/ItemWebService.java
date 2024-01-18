package org.example.common;

import java.util.List;

public interface ItemWebService {
    List<Item> getItems();

    String createItem(String name,  String description, int level, int power, int price);
    void updateItem(String name,  String description, int level, int power, int price);
    void deleteItem(String name);
}
