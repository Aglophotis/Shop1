package ru.mirea.data.shop.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mirea.data.shop.entities.Currency;
import ru.mirea.data.shop.repository.BalanceRepository;
import ru.mirea.data.shop.repository.CurrencyRepository;
import ru.mirea.data.shop.repository.ItemRepository;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class SqlInit {

    @Autowired
    SqlHelper sqlHelper;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @PostConstruct
    private void init() {
        if (isExistenceOfTable("items")) {
            return;
        }
        createTableOfCart();
        createTableOfItems();
        itemRepository.insertIntoItems("Dog-collar", "Stuff", 200, 1);
        itemRepository.insertIntoItems("Ball", "Stuff", 100, 4);
        itemRepository.insertIntoItems("Cat", "Pet", 5299, 1);
        itemRepository.insertIntoItems("Milk", "Stuff", 150, 20);
        itemRepository.insertIntoItems("Rabbit", "Pet", 1000, 100);
        itemRepository.insertIntoItems("Food", "Stuff", 300, 2);
        itemRepository.insertIntoItems("Dog", "Pet", 3200, 5);
        createTableCurrency();
        currencyRepository.insertIntoCurrency("Ruble", 1);
        currencyRepository.insertIntoCurrency("Dollar", 63.4d);
        currencyRepository.insertIntoCurrency("Euro", 74.9d);
        createTableOfBalance();
        createBalanceForUser(1);
    }

    private int createBalanceForUser(int idAuthor) {
        List<Currency> list = currencyRepository.getAllCurrencies();
        for (Currency currency : list) {
            if (balanceRepository.insertIntoBalance(idAuthor, currency.getId(), 0d) == -1)
                return -1;
        }
        return 1;
    }

    private boolean isExistenceOfTable(String name) {
        String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name= ?";
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next())
                return false;
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private void createTableOfItems() {
        String sql = "CREATE TABLE IF NOT EXISTS items (\n"
                + "	id integer PRIMARY KEY,\n"
                + " type text NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + "	price integer NOT NULL,\n"
                + "	count integer NOT NULL\n"
                + ");";
        try (Statement stmt = sqlHelper.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Items' successful create");
    }

    private void createTableOfCart() {
        String sql = "CREATE TABLE IF NOT EXISTS cart (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	id_author integer NOT NULL,\n"
                + "	id_item integer NOT NULL\n"
                + ");";
        try (Statement stmt = sqlHelper.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Cart' successful create");
    }

    private void createTableOfBalance() {
        String sql = "CREATE TABLE IF NOT EXISTS balance (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	id_author integer NOT NULL,\n"
                + " id_currency integer NOT NULL, \n"
                + "	balance real NOT NULL\n"
                + ");";
        try (Statement stmt = sqlHelper.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Balance' successful create");
    }

    private void createTableCurrency() {
        String sql = "CREATE TABLE IF NOT EXISTS currency (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	currency text NOT NULL,\n"
                + "	exchange_rate real NOT NULL\n"
                + ");";
        try (Statement stmt = sqlHelper.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: connection problems");
        }
        System.out.println("Table 'Currency' successful create");
    }
}
