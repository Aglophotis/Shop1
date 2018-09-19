package ru.mirea.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLWorker {
    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:C:\\DB\\stuffs.db";

    public void run(){
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Connection has been established");
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            return;
        }
        createTable(connection);
        try {
            connection.close();
            System.out.println("Connection has been closed");
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
            return;
        }
    }

    private void createTable(Connection conn){
        try {
            String sql = "CREATE TABLE IF NOT EXISTS stuffs (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL,\n"
                    + "	price integer NOT NULL,\n"
                    + "	count integer NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SQLWorker app = new SQLWorker();
        app.run();
    }
}