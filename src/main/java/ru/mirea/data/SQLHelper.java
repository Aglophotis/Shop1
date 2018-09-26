package ru.mirea.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

@Component
public class SQLHelper {
    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:c://DB/shop.db";
    private Connection conn;
    private boolean isRun = false;

    //Service methods

    @PostConstruct
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
            init();
        }
    }

    private void init(){
        createTableOfCart();
        createTableOfItems();
        insertIntoItems("Dog-collar", "Stuff", 200, 1);
        insertIntoItems("Ball", "Stuff", 100, 4);
        insertIntoItems("Cat", "Pet", 5299, 1);
        insertIntoItems("Milk", "Stuff", 150, 20);
        insertIntoItems("Rabbit", "Pet", 1000, 100);
        insertIntoItems("Food", "Stuff", 300, 2);
        insertIntoItems("Dog", "Pet", 3200, 5);
        createTableCurrency();
        insertIntoCurrency("Ruble", 1);
        insertIntoCurrency("Dollar", 63.4d);
        insertIntoCurrency("Euro", 74.9d);
        createTableOfBalance();
        createBalanceForUser(1);
    }

    public int createBalanceForUser(int idAuthor){
        ArrayList<Integer> list = selectDistinctColumnValues("id", "currency");
        for (int idCurrency : list){
            if (insertIntoBalance(idAuthor, idCurrency, 0d) == -1)
                return -1;
        }
        return 1;
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
            return;
        }
    }

    //Select methods


    public int selectColumnValueById(String columnLabel, String tableLabel, int id){
        String sql = "SELECT " + columnLabel + " FROM " + tableLabel + " WHERE id = " + id;
        try (Statement stmt  = conn.createStatement()){
            ResultSet rs  = stmt.executeQuery(sql);
            if (!rs.next()){
                return -1;
            }
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getCountOfRowsFromCart(int value) {
        String sql = "SELECT count(*) FROM cart WHERE id_item = " + value;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Integer> selectDistinctColumnValues(String columnLabel, String tableLabel){
        String sql = "SELECT DISTINCT " + columnLabel + " FROM " + tableLabel;
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

    public ArrayList<Integer> selectDistinctColumnValuesByAuthorID(String columnLabel, String tableLabel, int idAuthor){
        String sql = "SELECT DISTINCT " + columnLabel + " FROM " + tableLabel + " WHERE id_author = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, idAuthor);
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

    public ObjectNode selectAllFromItems(String type){
        String sql = "SELECT * FROM items WHERE type = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, type);
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForItem(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode selectAllFromItems(){
        String sql = "SELECT * FROM items";
        try (Statement stmt  = conn.createStatement()){
            ResultSet rs  = stmt.executeQuery(sql);
            return createJSONForItem(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode selectConcreteFromItemsById(String type, int id){
        String sql = "SELECT * FROM items WHERE type = ? AND id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, type);
            pstmt.setInt(2, id);
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForItem(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode selectAllFromCart(int idAuthor){
        String sql = "SELECT * FROM cart WHERE id_author = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, idAuthor);
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForCart(rs, idAuthor);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double calculateTotalAmount(int idAuthor){
        double totalAmount = 0;
        for (int id : selectDistinctColumnValuesByAuthorID("id_item", "cart", idAuthor)) {
            int nCountInCart = getCountOfRowsFromCart(id);
            totalAmount += nCountInCart * selectColumnValueById("price", "items", id);
        }
        return totalAmount;
    }

    public ObjectNode selectAllFromCurrency(){
        String sql = "SELECT * FROM currency";
        try (Statement stmt  = conn.createStatement()){
            ResultSet rs  = stmt.executeQuery(sql);
            return createJSONForCurrency(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode selectConcreteFromCurrencyByID(int id){
        String sql = "SELECT * FROM currency WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForCurrency(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObjectNode selectAllFromBalance(int idAuthor){
        String sql = "SELECT * FROM balance WHERE id_author = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, idAuthor);
            ResultSet rs  = pstmt.executeQuery();
            return createJSONForBalance(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double selectBalanceByCurrencyID(int idCurrency, int idAuthor){
        String sql = "SELECT balance FROM balance WHERE id_author = ? AND id_currency = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, idAuthor);
            pstmt.setInt(2, idCurrency);
            ResultSet rs  = pstmt.executeQuery();
            return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1d;
        }
    }

    //Create methods

    public void createTableOfItems(){
        String sql = "CREATE TABLE IF NOT EXISTS items (\n"
                + "	id integer PRIMARY KEY,\n"
                + " type text NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + "	price integer NOT NULL,\n"
                + "	count integer NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Items' successful create");
    }

    public void createTableOfCart(){
        String sql = "CREATE TABLE IF NOT EXISTS cart (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	id_author integer NOT NULL,\n"
                + "	id_item integer NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Cart' successful create");
    }

    public void createTableOfBalance(){
        String sql = "CREATE TABLE IF NOT EXISTS balance (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	id_author integer NOT NULL,\n"
                + " id_currency integer NOT NULL, \n"
                + "	balance real NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Balance' successful create");
    }

    public void createTableCurrency(){
        String sql = "CREATE TABLE IF NOT EXISTS currency (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	currency text NOT NULL,\n"
                + "	exchange_rate real NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Currency' successful create");
    }

    //Insert methods

    private int insertIntoItems(String name, String type, int price, int count) {
        String sql = "INSERT INTO items(name, type, price, count) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    private int insertIntoCurrency(String currency, double exchange_rate){
        String sql = "INSERT INTO currency(currency, exchange_rate) VALUES(?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currency);
            pstmt.setDouble(2, exchange_rate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
        return 1;
    }

    private int insertIntoBalance(int idAuthor, int idCurrency, double balance){
        String sql = "INSERT INTO balance(id_author, id_currency, balance) VALUES(?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAuthor);
            pstmt.setInt(2, idCurrency);
            pstmt.setDouble(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
        return 1;
    }

    //Update methods

    public int updateColumnValueByID(String tableLabel, String columnLabel, int columnValue, int id){
        String sql = "UPDATE " + tableLabel + " SET " + columnLabel + " = ? WHERE id = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(2, id);
            pstmt.setInt(1, columnValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public int updateBalanceByCurrencyID(int idCurrency, int idAuthor, double value){
        String sql = "UPDATE balance SET balance = ? WHERE id_currency = ? AND id_author = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setDouble(1, value);
            pstmt.setInt(2, idCurrency);
            pstmt.setInt(3, idAuthor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    //Create JSON methods

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

    private ObjectNode createJSONForCart(ResultSet rs, int idAuthor){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode itemsArray = mapper.createArrayNode();
        try {
            while (rs.next()) {
                ObjectNode stuff = mapper.createObjectNode();
                stuff.put("id", rs.getInt("id"));
                stuff.put("id_item", rs.getInt("id_item"));
                stuff.put("id_author", rs.getInt("id_author"));
                itemsArray.add(stuff);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("cart", itemsArray);
        objectNode.putPOJO("Total amount in rubles", calculateTotalAmount(idAuthor));
        return objectNode;
    }

    private ObjectNode createJSONForCurrency(ResultSet rs){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode itemsArray = mapper.createArrayNode();
        DecimalFormat f = new DecimalFormat("###0.00");
        try {
            while (rs.next()) {
                ObjectNode stuff = mapper.createObjectNode();
                stuff.put("id", rs.getInt("id"));
                stuff.put("currency", rs.getString("currency"));
                stuff.put("exchange_rate", f.format(rs.getDouble("exchange_rate")));
                itemsArray.add(stuff);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("Currencies", itemsArray);
        return objectNode;
    }

    private ObjectNode createJSONForBalance(ResultSet rs){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode itemsArray = mapper.createArrayNode();
        DecimalFormat f = new DecimalFormat("###0.00");
        try {
            while (rs.next()) {
                ObjectNode stuff = mapper.createObjectNode();
                stuff.put("id", rs.getInt("id"));
                stuff.put("id_author", rs.getInt("id_author"));
                stuff.put("id_currency", rs.getInt("id_currency"));
                stuff.put("balance", f.format(rs.getDouble("balance")));
                itemsArray.add(stuff);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("balance", itemsArray);
        return objectNode;
    }

    //Delete methods

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



    public int deleteFromTableByIDAndAuthorID(String tableLabel, int id, int idAuthor){
        String sql = "DELETE FROM " + tableLabel + " WHERE id = ? AND id_author = ?";
        try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.setInt(2, idAuthor);
            if (pstmt.executeUpdate() == 0){
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }


}