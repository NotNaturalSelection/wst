package org.example;

import org.example.common.ConnectionManager;
import javax.annotation.Resource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionManager implements ConnectionManager {
    @Resource(lookup = "jdbc/dataSource")
    private DataSource ds;
    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public DataSourceConnectionManager(String jndi) throws NamingException {
        ds = (DataSource) new InitialContext().lookup(jndi);
    }
}
