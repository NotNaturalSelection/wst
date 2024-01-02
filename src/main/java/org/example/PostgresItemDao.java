package org.example;

import org.example.common.ConnectionManager;
import org.example.common.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresItemDao {
    private final ConnectionManager cm;

    public PostgresItemDao(ConnectionManager cm) {
        this.cm = cm;
    }

    public List<Item> getItems() {
        List<Item> items = new LinkedList<>();
        try (Connection connection = cm.getConnection()){
            ResultSet rs = connection.createStatement().executeQuery("select * from public.items;");
            while (rs.next()) {
                System.out.println("Fetched item name: " + rs.getString("name"));
                items.add(new Item(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getInt("level"),
                        rs.getInt("power"))
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of getItems method", e);
        }
        return items;
    }
}
