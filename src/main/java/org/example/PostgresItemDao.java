package org.example;

import org.example.common.ConnectionManager;
import org.example.common.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public void saveItem(Item item) {
        try (Connection connection = cm.getConnection()){
            PreparedStatement st = connection.prepareStatement("insert into public.items(name, description, level, price, power) values(?, ?, ?, ?, ?);");
            st.setString(1, item.getName());
            st.setString(2, item.getDescription());
            st.setInt(3, item.getLevel());
            st.setInt(4, item.getPrice());
            st.setInt(5, item.getPower());
            st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of saveItem method", e);
        }
    }

    public boolean updateItem(Item item) {
        int rowsModified = 0;
        try (Connection connection = cm.getConnection()){
            PreparedStatement st = connection.prepareStatement("update public.items set description = ?, level = ?, price = ?, power = ? where name = ?;");
            st.setString(1, item.getDescription());
            st.setInt(2, item.getLevel());
            st.setInt(3, item.getPrice());
            st.setInt(4, item.getPower());
            st.setString(5, item.getName());
            rowsModified = st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of saveItem method", e);
        }
        return rowsModified == 1;
    }

    public boolean deleteItemByName(String name) {
        int rowsDeleted = 0;
        try (Connection connection = cm.getConnection()){
            PreparedStatement st = connection.prepareStatement("delete from public.items where name=?;");
            st.setString(1, name);
            rowsDeleted = st.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of deleteItem method", e);
        }
        return rowsDeleted == 1;
    }

}
