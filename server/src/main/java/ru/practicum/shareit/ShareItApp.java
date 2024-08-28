package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories()
@EnableTransactionManagement
@EnableCaching
public class ShareItApp {

    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);
    }
}
