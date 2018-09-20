package ru.mirea.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.*;

public class SQLWorker {
    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:C:\\DB\\stuffs.db";
    private Connection conn;

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
        conn = connection;
        /*createTable();
        insert("Dog-collar", 200, 1);
        insert("Ball", 100, 4);
        insert("Milk", 150, 20);
        insert("Food", 300, 2);
        stop();*/
    }

    private void createTable(){
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

    public void insert(String name, int price, int count) {
        String sql = "INSERT INTO stuffs(name, price, count) VALUES(?,?,?)";
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, price);
                pstmt.setInt(3, count);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int selectCount(int id){
        String sql = "SELECT * FROM stuffs WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs  = pstmt.executeQuery();
            return rs.getInt("count");
        } catch (SQLException e) {
            return -1;
        }
    }

    public int updateCount(int id, int count){
        String sql = "UPDATE stuffs SET count = ? WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, count);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

    public ObjectNode selectAll(){
        String sql = "SELECT * FROM stuffs";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode stuffsArray = mapper.createArrayNode();
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                ObjectNode stuff = mapper.createObjectNode();
                stuff.put("id", rs.getInt("id"));
                stuff.put("name", rs.getString("name"));
                stuff.put("price", rs.getInt("price"));
                stuff.put("count", rs.getInt("count"));
                stuffsArray.add(stuff);
            }
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.putPOJO("stuffs", stuffsArray);
            return objectNode;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void stop(){
        try {
            conn.close();
            System.out.println("Connection has been closed");
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
            return;
        }
    }
}