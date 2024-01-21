package org.example.rest;

import org.example.PostgresItemDao;
import org.example.common.Item;
import org.example.ws.EmptyIdentifierException;
import org.example.ws.ResourceForbiddenException;
import org.example.ws.ServiceException;
import org.example.ws.ThrottlingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(path = "/items")
public class ItemController  {

    private final PostgresItemDao itemDao;

    private static final String PASSWORD = "password";

    private static final Integer REQUEST_LIMIT = 10;
    private final AtomicInteger count = new AtomicInteger(0);

    private void onReq() {
        int newValue = count.getAndUpdate(operand -> {
            if (operand >= REQUEST_LIMIT) {
                return operand;
            }
            return operand + 1;
        });

        if (newValue > REQUEST_LIMIT) {
            throw new ThrottlingException("Limit of 10 concurrent requests has reached");
        }
    }

    private void onResp() {
        count.decrementAndGet();
    }

    private void checkAuthenticated(String authHeader) {
        if (!authHeader.contains("Basic: ")) {
            throw new ResourceForbiddenException();
        }
        authHeader = authHeader.replaceFirst("Basic: ", "");
        if (!new String(Base64.getDecoder().decode(authHeader)).equals(PASSWORD)) {
            throw new ResourceForbiddenException();
        }
    }

    @Autowired
    public ItemController(PostgresItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @GetMapping
    public List<Item> getItems() throws ServiceException {
        onReq();
        var res = itemDao.getItems();
        onResp();
        return  res;
    }

    @PostMapping
    public String createItem(@RequestHeader("Authentication") String auth, @RequestBody Item item) {
        checkAuthenticated(auth);
        if (item.getName().isBlank()) {
            throw new EmptyIdentifierException("Item should not have empty identifier 'name'");
        }
        itemDao.saveItem(item);
        return item.getName();
    }

    @PutMapping
    public void updateItem(@RequestHeader("Authentication") String auth, @RequestBody Item item) {
        checkAuthenticated(auth);
        if (item.getName().isBlank()) {
            throw new EmptyIdentifierException("Item should not have empty identifier 'name'");
        }
        itemDao.updateItem(item);
    }

    @DeleteMapping
    public void deleteItem(@RequestHeader("Authentication") String auth, @RequestParam("name") String name) {
        checkAuthenticated(auth);
        if (name.isBlank()) {
            throw new EmptyIdentifierException("Item should not have empty identifier 'name'");
        }
        itemDao.deleteItemByName(name);
    }
}
