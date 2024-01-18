package org.example;

import org.example.common.ConnectionManager;
import org.example.common.Item;
import org.example.errors.ItemServiceFault;
import org.example.ws.IdentifierAlreadyUsedException;
import org.example.ws.ItemNotFoundException;
import org.example.ws.ServiceException;

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

    public List<Item> getItems() throws ServiceException {
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
            throw new ServiceException("Unexpected exception", ItemServiceFault.defaultInstance(), e);
        }
        return items;
    }

    public void saveItem(Item item) throws IdentifierAlreadyUsedException {
        try (Connection connection = cm.getConnection()){
            PreparedStatement st = connection.prepareStatement("insert into public.items(name, description, level, price, power) values(?, ?, ?, ?, ?);");
            st.setString(1, item.getName());
            st.setString(2, item.getDescription());
            st.setInt(3, item.getLevel());
            st.setInt(4, item.getPrice());
            st.setInt(5, item.getPower());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new IdentifierAlreadyUsedException("Item with name '"+item.getName()+"' already exists", ItemServiceFault.defaultInstance(), e);
        }
    }

    public void updateItem(Item item) throws ItemNotFoundException {
        try (Connection connection = cm.getConnection()){
            PreparedStatement st = connection.prepareStatement("update public.items set description = ?, level = ?, price = ?, power = ? where name = ?;");
            st.setString(1, item.getDescription());
            st.setInt(2, item.getLevel());
            st.setInt(3, item.getPrice());
            st.setInt(4, item.getPower());
            st.setString(5, item.getName());
            if (st.executeUpdate() == 0) {
                throw new ItemNotFoundException("Item with name '"+item.getName()+"' is not found", ItemServiceFault.defaultInstance());
            }
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of saveItem method", e);
        }
    }

    public Item getItemByName(String name) throws ItemNotFoundException, ServiceException {
        try (Connection connection = cm.getConnection()){
            ResultSet rs = connection.createStatement().executeQuery("select * from public.items;");
            if (!rs.next()) {
                throw new ItemNotFoundException("Item with name '" + name + "' is not found", ItemServiceFault.defaultInstance());
            }

            return new Item(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("price"),
                    rs.getInt("level"),
                    rs.getInt("power"));
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of getItem method", e);
            throw new ServiceException("Unexpected exception", ItemServiceFault.defaultInstance(), e);
        }
    }
    public void deleteItemByName(String name) throws ItemNotFoundException {
        try (Connection connection = cm.getConnection()){
            PreparedStatement st = connection.prepareStatement("delete from public.items where name=?;");
            st.setString(1, name);
            if(st.executeUpdate() == 0) {
                throw new ItemNotFoundException("Item with name '" + name + "' is not found", ItemServiceFault.defaultInstance());
            }
        } catch (SQLException e) {
            Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Unexpected ending of deleteItem method", e);
        }
    }

}
