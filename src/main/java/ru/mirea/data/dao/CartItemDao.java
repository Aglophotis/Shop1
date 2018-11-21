package ru.mirea.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mirea.data.data.SqlHelper;
import ru.mirea.data.entities.CartItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CartItemDao {

    @Autowired
    SqlHelper sqlHelper;

    public List<CartItem> getAllCartItems() {
        String sql = "SELECT * FROM cart WHERE id_author = '1'";
        return executeSql(sql);
    }

    public List<CartItem> getDistinctCartItems() {
        String sql = "SELECT DISTINCT * FROM cart WHERE id_author = '1'";
        List<CartItem> list = executeSql(sql);
        if (list == null) {
            return null;
        }
        ArrayList<CartItem> resultList = new ArrayList<>();
        for (CartItem cartItem : list) {
            boolean isContains = false;
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).getIdItem() == cartItem.getIdItem()) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                resultList.add(cartItem);
            }
        }
        return resultList;
    }

    public void clearCart() {
        String sql = "DELETE FROM cart WHERE id_author = '1'";
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getItemsCountInCart(int id) {
        String sql = "SELECT count(*) FROM cart WHERE id_item = ?";
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int insertIntoCart(int id) {
        String sql = "INSERT INTO cart(id_item, id_author) VALUES(?, '1')";
        return executeUpdateSql(id, sql);
    }

    public int deleteFromCartById(int id){
        String sql = "DELETE FROM cart WHERE id = ? AND id_author = '1'";
        return executeUpdateSql(id, sql);
    }

    private int executeUpdateSql(int arg, String sql) {
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            pstmt.setInt(1, arg);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private List<CartItem> executeSql(String sql) {
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            return createCartItemsList(rs, 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<CartItem> createCartItemsList(ResultSet rs, int idAuthor) {
        ArrayList<CartItem> list = new ArrayList<>();
        try {
            while (rs.next()) {
                CartItem cartItem = new CartItem(rs.getInt("id"), rs.getInt("id_item")
                        , idAuthor);
                list.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
