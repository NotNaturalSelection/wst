//package org.example.ws;
//
//import jakarta.jws.WebMethod;
//import jakarta.jws.WebParam;
//import jakarta.jws.WebService;
//import org.example.DataSourceConnectionManager;
//import org.example.PostgresItemDao;
//import org.example.common.ConnectionManager;
//import org.example.common.Item;
//import org.example.common.ItemWebService;
//import org.example.errors.ItemServiceFault;
//
//import javax.naming.NamingException;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@WebService(name = "SoapItemWebService", serviceName = "SoapItemWebService")
//public class SoapItemWebService implements ItemWebService {
//
//    private static final String DATASOURCE_JNDI = "jdbc/dataSource";
//    private final PostgresItemDao itemDao;
//
//    public SoapItemWebService(ConnectionManager cm) throws NamingException {
//        if (cm == null) {
//            try {
//                cm = new DataSourceConnectionManager(DATASOURCE_JNDI);
//            } catch (Throwable e) {
//                Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Exception:", e);
//                throw e;
//            }
//        }
//        itemDao = new PostgresItemDao(cm);
//    }
//
//    public SoapItemWebService() throws NamingException {
//        this(null);
//    }
//
//    @Override
//    @WebMethod(operationName = "getItems")
//    public List<Item> getItems() throws ServiceException {
//        return itemDao.getItems();
//    }
//
//    @Override
//    @WebMethod(operationName = "getItem")
//    public Item getItem(@WebParam(name = "name") String name) throws ItemNotFoundException, ServiceException, EmptyIdentifierException {
//        if (name == null || name.isBlank()) {
//            throw new EmptyIdentifierException("Identifier 'name' should not be empty", ItemServiceFault.defaultInstance());
//        }
//        return itemDao.getItemByName(name);
//    }
//
//
//    @Override
//    @WebMethod(operationName = "createItem")
//    public String createItem(@WebParam(name = "name") String name, @WebParam(name = "description") String description, @WebParam(name = "level") int level, @WebParam(name = "power") int power, @WebParam(name = "price") int price) throws ServiceException, IdentifierAlreadyUsedException, EmptyIdentifierException {
//        if (name == null || name.isBlank()) {
//            throw new EmptyIdentifierException("Identifier 'name' should not be empty", ItemServiceFault.defaultInstance());
//        }
//        itemDao.saveItem(new Item(name, description, price, level, power));
//        return name;
//    }
//
//    @Override
//    @WebMethod(operationName = "updateItem")
//    public void updateItem(@WebParam(name = "name") String name, @WebParam(name = "description") String description, @WebParam(name = "level") int level, @WebParam(name = "power") int power, @WebParam(name = "price") int price) throws ItemNotFoundException, EmptyIdentifierException {
//        if (name == null || name.isBlank()) {
//            throw new EmptyIdentifierException("Identifier 'name' should not be empty", ItemServiceFault.defaultInstance());
//        }
//        itemDao.updateItem(new Item(name, description, price, level, power));
//    }
//
//    @Override
//    @WebMethod(operationName = "deleteItem")
//    public void deleteItem(@WebParam(name = "name") String name) throws ItemNotFoundException, EmptyIdentifierException {
//        if (name == null || name.isBlank()) {
//            throw new EmptyIdentifierException("Identifier 'name' should not be empty", ItemServiceFault.defaultInstance());
//        }
//        itemDao.deleteItemByName(name);
//    }
//}
