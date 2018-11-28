package ru.mirea.data.shop.data;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class SqlHelper {
    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:c://DB/shop.db";
    private Connection conn;
    private boolean isRun = false;

    @PostConstruct
    public void run(){
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Connection has been established");
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            return;
        }
        conn = connection;
        isRun = true;
    }

    public Connection getConnection(){
        return conn;
    }

    public boolean connectionIsRun(){
        return isRun;
    }

    public void stop(){
        try {
            conn.close();
            isRun = false;
            System.out.println("Connection has been closed");
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
        }
    }
}