package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getCart(){
        return sqlHelper.selectAllFromCart(1);
    }

    public String deleteItemFromCart(int id){
        int err = sqlHelper.deleteFromTableByIDAndAuthorID("cart", id, 1);
        if (err == -1){
            return "Error: connection problem";
        } else if (err == 1){
            return "The stuff was successfully removed from cart";
        } else {
            return "Error: stuff wasn't found in cart";
        }
    }

    public String putItemToCart(int id){
        int nCountInStuffs = sqlHelper.selectColumnValueById("count", "items", id);
        if (nCountInStuffs == -1) return "Error: id wasn't found";
        if (nCountInStuffs == 0) return "The stuffs are over";
        int nCountInCart = sqlHelper.getCountOfRowsFromCart(id);
        if (nCountInCart < nCountInStuffs){
            if (sqlHelper.insertIntoCart(id, 1) == -1)
                return "Error: connection problems";
        } else {
            return "The stuffs are over";
        }
        return "Stuff was been added to cart";
    }

    public String paymentOfCart(){
        ArrayList<Integer> uniqItemId = sqlHelper.selectDistinctColumnValues("id_item", "cart");
        int paymentAmount = 0;
        if (uniqItemId.isEmpty()){
            return "Error: cart is empty";
        }
        for (int id : uniqItemId) {
            int nCountInStuffs = sqlHelper.selectColumnValueById("count","items", id);
            int nCountInCart = sqlHelper.getCountOfRowsFromCart(id);
            if (nCountInCart > nCountInStuffs) {
                return "Error: the quantity of the stuffs(" + id + ") has been changed.";
            }
            paymentAmount += nCountInCart * sqlHelper.selectColumnValueById("price", "items", id);
        }

        double balance = 0;
        for (int idCurrency : sqlHelper.selectDistinctColumnValues("id", "currency")){
            double exchangeRate = sqlHelper.selectColumnValueById("exchange_rate", "currency", idCurrency);
            balance += sqlHelper.selectBalanceByCurrencyID(idCurrency, 1)*exchangeRate;
        }

        if (balance < paymentAmount){
            return "You don't have enough money";
        }

        for (int idCurrency : sqlHelper.selectDistinctColumnValues("id", "currency")) {
            double exchangeRate = sqlHelper.selectColumnValueById("exchange_rate", "currency", idCurrency);
            double currencyBalance = sqlHelper.selectBalanceByCurrencyID(idCurrency, 1)*exchangeRate;
            if (currencyBalance >= paymentAmount) {
                sqlHelper.updateBalanceByCurrencyID(idCurrency, 1, (currencyBalance-paymentAmount)/exchangeRate);
                paymentAmount = 0;
                break;
            } else {
                sqlHelper.updateBalanceByCurrencyID(idCurrency, 1, 0);
                paymentAmount -= currencyBalance;
            }
        }

        for (int id : uniqItemId) {
            int nCountInStuffs = sqlHelper.selectColumnValueById("count","items", id);
            int nCountInCart = sqlHelper.getCountOfRowsFromCart(id);
            if (nCountInCart <= nCountInStuffs) {
                if (sqlHelper.updateColumnValueByID("items", "count", nCountInStuffs - nCountInCart, id) == -1){
                    return "Error: connection problem";
                }
            }
        }
        sqlHelper.clearTable("cart");
        return "The payment has been successfully completed";
    }
}
