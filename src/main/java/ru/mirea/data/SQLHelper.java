package ru.mirea.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;

@Component
public class SQLHelper {
    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:c://DB/shop.db";
    private Connection conn;
    private boolean isRun = false;

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
        isRun = true;
        if (!isExistenceOfTable("stuffs")){
            createTableOfStuffs();
            insertIntoStuffs("Dog-collar", 200, 1);
            insertIntoStuffs("Ball", 100, 4);
            insertIntoStuffs("Milk", 150, 20);
            insertIntoStuffs("Food", 300, 2);
        }
    }

    public boolean isRun(){
        return isRun;
    }

    public boolean isExistenceOfTable(String name) {
        String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next())
                return false;
        } catch (SQLException e){
            return false;
        }
        return true;
    }

    public void createTableOfStuffs(){
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
            System.out.println("Error: connection problems");
        }
        System.out.println("Table successful create");
    }

    public int createTableOfCart(){
        try {
            String sql = "CREATE TABLE IF NOT EXISTS cart (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	id_author integer NOT NULL,\n"
                    + "	id_item integer NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: connection problems");
            return -1;
        }
        System.out.println("Table successful create");
        return 1;
    }

    public int getCountOfRowsFromCart(int value){
        try {
            String sql = "SELECT count(*) FROM cart WHERE id_item = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, value);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public int insertIntoStuffs(String name, int price, int count) {
        String sql = "INSERT INTO stuffs(name, price, count) VALUES(?,?,?)";
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, price);
            pstmt.setInt(3, count);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
        return 1;
    }

    public int insertIntoCart(int id_item, int id_author) {
        String sql = "INSERT INTO cart(id_item, id_author) VALUES(?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_item);
            pstmt.setInt(2, id_author);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
        return 1;
    }

    public int selectColumnValueFromStuff(String columnLabel, int id){
        String sql = "SELECT * FROM stuffs WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs  = pstmt.executeQuery();
            return rs.getInt(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public ArrayList<Integer> selectDistinctItemIDFromCart(){
        String sql = "SELECT DISTINCT id_item FROM cart";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            ArrayList<Integer> al = new ArrayList<>();
            while (rs.next()){
                al.add(rs.getInt(1));
            }
            return al;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int updateCountInStuffs(int id, int count){
        String sql = "UPDATE stuffs SET count = ? WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, count);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public ObjectNode selectAllFromStuffs(){
        if (!isExistenceOfTable("stuffs")){
            createTableOfStuffs();
        }
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
            e.printStackTrace();
            return null;
        }
    }

    public int clearTable(String tableName){
        String sql = "DELETE FROM " + tableName;
        try(Statement pstmt = conn.createStatement()) {
            pstmt.executeUpdate(sql);
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public ObjectNode selectAllFromCart(){
        if (!isExistenceOfTable("cart")){
            createTableOfCart();
        }
        String sql = "SELECT * FROM cart";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode stuffsArray = mapper.createArrayNode();
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                ObjectNode stuff = mapper.createObjectNode();
                stuff.put("id", rs.getInt("id"));
                stuff.put("id_item", rs.getInt("id_item"));
                stuff.put("id_author", rs.getInt("id_author"));
                stuffsArray.add(stuff);
            }
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.putPOJO("cart", stuffsArray);
            return objectNode;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteStuffFromCart(int id){
        String sql = "DELETE FROM cart WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            if (pstmt.executeUpdate() == 0){
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public void stop(){
        try {
            conn.close();
            isRun = false;
            System.out.println("Connection has been closed");
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
            return;
        }
    }
}