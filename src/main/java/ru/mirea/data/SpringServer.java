package ru.mirea.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringServer{

    @Autowired
    private StuffService stuffService;

    @PostConstruct
    public void init(){
        stuffService.openConnectionToDB();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringServer.class);
    }

}