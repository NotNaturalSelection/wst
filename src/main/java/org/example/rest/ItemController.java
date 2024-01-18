package org.example.rest;

import org.example.PostgresItemDao;
import org.example.common.Item;
import org.example.ws.EmptyIdentifierException;
import org.example.ws.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class ItemController  {

    private final PostgresItemDao itemDao;

    @Autowired
    public ItemController(PostgresItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @GetMapping
    public List<Item> getItems() throws ServiceException {
        return itemDao.getItems();
    }

    @PostMapping
    public String createItem(@RequestBody Item item) {
        if (item.getName().isBlank()) {
            throw new EmptyIdentifierException("Item should not have empty identifier 'name'");
        }
        itemDao.saveItem(item);
        return item.getName();
    }

    @PutMapping
    public void updateItem(@RequestBody Item item) {
        if (item.getName().isBlank()) {
            throw new EmptyIdentifierException("Item should not have empty identifier 'name'");
        }
        itemDao.updateItem(item);
    }

    @DeleteMapping
    public void deleteItem(@RequestParam("name") String name) {
        if (name.isBlank()) {
            throw new EmptyIdentifierException("Item should not have empty identifier 'name'");
        }
        itemDao.deleteItemByName(name);
    }
}
