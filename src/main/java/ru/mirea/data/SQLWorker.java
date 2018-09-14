package ru.mirea.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLWorker {
    private String userName = "root";
    private String password = "xxxx";
    private String dbms = "mysql";
    private String serverName = "127.0.0.1";
    private String portNumber = "3306";

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        if (this.dbms.equals("mysql")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + this.dbms + "://" +
                            this.serverName +
                            ":" + this.portNumber + "/?verifyServerCertificate=false&useSSL=true",
                    connectionProps);
        } else {
            System.out.println("Unknown database");
            return null;
        }
        System.out.println("Connected to database");
        return conn;
    }
}
