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
        if (!isExistenceOfTable("items")){
            createTableOfItems();
            insertIntoItems("Dog-collar", "Stuff", 200, 1);
            insertIntoItems("Ball", "Stuff", 100, 4);
            insertIntoItems("Cat", "Pet", 5299, 1);
            insertIntoItems("Milk", "Stuff", 150, 20);
            insertIntoItems("Rabbit", "Pet", 1000, 100);
            insertIntoItems("Food", "Stuff", 300, 2);
            insertIntoItems("Dog", "Pet", 3200, 5);
        }
    }

    public boolean connectionIsRun(){
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

    public void createTableOfItems(){
        try {
            String sql = "CREATE TABLE IF NOT EXISTS items (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + " type text NOT NULL,\n"
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

    public int insertIntoItems(String name, String type, int price, int count) {
        String sql = "INSERT INTO items(name, type, price, count) VALUES(?,?,?,?)";
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, price);
            pstmt.setInt(4, count);
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
        String sql = "SELECT * FROM items WHERE id = ?";
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

    public int updateCountInItems(int id, int count){
        String sql = "UPDATE items SET count = ? WHERE id = ?";
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

    public ObjectNode selectAllFromItems(String type){
        if (!isExistenceOfTable("items")){
            createTableOfItems();
        }
        String sql = "SELECT * FROM items WHERE type = " + type;
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForItem(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode selectAllFromItems(){
        if (!isExistenceOfTable("items")){
            createTableOfItems();
        }
        String sql = "SELECT * FROM items";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForItem(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ObjectNode createJSONForItem(ResultSet rs){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode itemsArray = mapper.createArrayNode();
        try{
            while (rs.next()) {
            ObjectNode stuff = mapper.createObjectNode();
            stuff.put("id", rs.getInt("id"));
            stuff.put("type", rs.getString("type"));
            stuff.put("name", rs.getString("name"));
            stuff.put("price", rs.getInt("price"));
            stuff.put("count", rs.getInt("count"));
            itemsArray.add(stuff);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("items", itemsArray);
        return objectNode;
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
            ArrayNode itemsArray = mapper.createArrayNode();
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                ObjectNode stuff = mapper.createObjectNode();
                stuff.put("id", rs.getInt("id"));
                stuff.put("id_item", rs.getInt("id_item"));
                stuff.put("id_author", rs.getInt("id_author"));
                itemsArray.add(stuff);
            }
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.putPOJO("cart", itemsArray);
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