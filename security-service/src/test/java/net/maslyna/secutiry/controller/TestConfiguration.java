//package net.maslyna.secutiry.controller;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class TestConfiguration {
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public PostgreSQLContainer<?> postgreSQLContainer() {
//        return new PostgreSQLContainer<>("postgres:latest");
//    }
//
//    @Bean
//    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
//        dataSource.setUsername(postgreSQLContainer.getUsername());
//        dataSource.setPassword(postgreSQLContainer.getPassword());
//        return dataSource;
//    }
//}
