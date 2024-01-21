package org.example;

import org.example.ws.SoapItemWebService;

import javax.xml.ws.Endpoint;

import javax.naming.NamingException;

//@SpringBootApplication
public class Server {
    public static void main(String[] args) {
//        SpringApplication.run(Server.class, args);
        String url = "http://localhost:8080/itemService";
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres";
        Endpoint.publish(url, new SoapItemWebService(new ManualConnectionManager(dbUrl, user, password)));
    }

//    @Bean
//    public DataSource dataSource() {
//        var ds = new PGSimpleDataSource();
//        ds.setUser("postgres");
//        ds.setPassword("postgres");
//        ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
//        return ds;
//    }
}