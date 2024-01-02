package org.example.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.DataSourceConnectionManager;
import org.example.PostgresItemDao;
import org.example.common.ConnectionManager;
import org.example.common.Item;
import org.example.common.ItemWebService;

import javax.naming.NamingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(name = "SoapItemWebService", serviceName = "SoapItemWebService")
public class SoapItemWebService implements ItemWebService {

    private static final String DATASOURCE_JNDI = "jdbc/dataSource";
    private final PostgresItemDao itemDao;

    public SoapItemWebService(ConnectionManager cm) throws NamingException {
        if (cm == null) {
            try {
                cm = new DataSourceConnectionManager(DATASOURCE_JNDI);
            } catch (Throwable e) {
                Logger.getLogger(PostgresItemDao.class.getName()).log(Level.WARNING, "Exception:", e);
                throw e;
            }
        }
        itemDao = new PostgresItemDao(cm);
    }

    public SoapItemWebService() throws NamingException {
        this(null);
    }

    @Override
    @WebMethod(operationName = "getItems")
    public List<Item> getItems() {
        return itemDao.getItems();
    }
}
