package com.bidhaa.configuration.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.bidhaa.repository"
})
@EnableTransactionManagement
public class H2TestProfileJPAConfig {

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        //test credentials
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/lending-test?createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;
    }
}