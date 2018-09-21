package ru.mirea.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class SpringServer{
    public static void main(String[] args) {
        SpringApplication.run(SpringServer.class);
        StuffService.openConnToBD();
    }
}