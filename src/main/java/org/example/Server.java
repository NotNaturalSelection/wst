package org.example;

import jakarta.xml.ws.Endpoint;
import org.example.ws.SoapItemWebService;

import javax.naming.NamingException;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws NamingException {
        String url = "http://localhost:42577/ws/ws/itemService";
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres";
        Endpoint.publish(url, new SoapItemWebService(new ManualConnectionManager(dbUrl, user, password)));
    }
}