package org.example;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public DataSource dataSource() {
        var ds = new PGSimpleDataSource();
        ds.setUser("postgres");
        ds.setPassword("postgres");
        ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
        return ds;
    }
}