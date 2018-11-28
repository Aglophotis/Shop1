package ru.mirea.data.shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mirea.data.shop.data.SqlHelper;
import ru.mirea.data.shop.entities.Balance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BalanceRepository {

    @Autowired
    SqlHelper sqlHelper;

    public int updateBalanceByCurrencyID(int idCurrency, double value){
        String sql = "UPDATE balance SET balance = ? WHERE id_currency = ? AND id_author = '1'";
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            pstmt.setDouble(1, value);
            pstmt.setInt(2, idCurrency);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Balance> getBalances(){
        String sql = "SELECT * FROM balance WHERE id_author = '1'";
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            return createBalancesList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Balance getBalanceByCurrencyId(int id){
        String sql = "SELECT * FROM balance WHERE id_author = '1' AND id_currency = ?";
        try (PreparedStatement pstmt  = sqlHelper.getConnection().prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs  = pstmt.executeQuery();
            return createBalancesList(rs).get(0);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertIntoBalance(int idAuthor, int idCurrency, double balance){
        String sql = "INSERT INTO balance(id_author, id_currency, balance) VALUES(?,?,?)";
        try (PreparedStatement pstmt = sqlHelper.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idAuthor);
            pstmt.setInt(2, idCurrency);
            pstmt.setDouble(3, balance);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    private ArrayList<Balance> createBalancesList(ResultSet rs){
        ArrayList<Balance> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Balance balance = new Balance(rs.getInt("id"), rs.getInt("id_author")
                        , rs.getInt("id_currency")
                        , rs.getDouble("balance"));
                list.add(balance);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
