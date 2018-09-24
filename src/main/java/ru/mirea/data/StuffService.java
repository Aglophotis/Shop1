package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StuffService {

    private SQLHelper sqlHelper = new SQLHelper();

    public ObjectNode getStuffs(){
        return sqlHelper.selectAllFromStuffs();
    }

    public ObjectNode getCart(){
        return sqlHelper.selectAllFromCart();
    }

    public String paymentOfCart(){
        if (!sqlHelper.isExistenceOfTable("cart")){
            return "Error: cart is empty";
        }
        ArrayList<Integer> uniqItemId = sqlHelper.selectDistinctItemIDFromCart();
        int paymentAmount = 0;
        if (uniqItemId.isEmpty()){
            return "Error: cart is empty";
        }
        for (int id : uniqItemId) {
            int nCountInStuffs = sqlHelper.selectColumnValueFromStuff("count", id);
            int nCountInCart = sqlHelper.getCountOfRowsFromCart(id);
            if (nCountInCart > nCountInStuffs) {
                return "Error: the quantity of the stuffs(" + id + ") has been changed.";
            }
            paymentAmount += nCountInCart * sqlHelper.selectColumnValueFromStuff("price", id);
        }
        for (int id : uniqItemId) {
            int nCountInStuffs = sqlHelper.selectColumnValueFromStuff("count", id);
            int nCountInCart = sqlHelper.getCountOfRowsFromCart(id);
            if (nCountInCart <= nCountInStuffs) {
                if (sqlHelper.updateCountInStuffs(id, nCountInStuffs - nCountInCart) == -1){
                    return "Error: connection problem";
                }
            }
        }
        sqlHelper.clearTable("cart");
        return "The payment has been successfully completed";
    }

    public String deleteStuffFromCart(int id){
        int err = sqlHelper.deleteStuffFromCart(id);
        if (err == -1){
            return "Error: connection problem";
        } else if (err == 1){
            return "The stuff was successfully removed from cart";
        } else {
            return "Error: stuff wasn't found in cart";
        }
    }

    public String putStuffToCart(int id){
        int nCountInStuffs = sqlHelper.selectColumnValueFromStuff("count", id);
        if (!sqlHelper.isExistenceOfTable("cart")){
            if (sqlHelper.createTableOfCart() == -1)
                return "Error: connection problem";
        }
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


    public void openConnectionToDB(){
        if (!sqlHelper.isRun())
            sqlHelper.run();
    }

    public void closeConnectionToDB(){
        if (sqlHelper.isRun())
            sqlHelper.stop();
    }
}
