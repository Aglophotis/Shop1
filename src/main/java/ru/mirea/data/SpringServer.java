package ru.mirea.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringServer{

    @Autowired
    private SQLHelper sqlHelper;

    @PostConstruct
    public void init(){
        sqlHelper.run();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringServer.class);
    }

}