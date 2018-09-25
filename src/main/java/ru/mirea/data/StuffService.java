package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StuffService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getStuffs(){
        return sqlHelper.selectAllFromItems("Stuff");
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
                if (sqlHelper.updateCountInItems(id, nCountInStuffs - nCountInCart) == -1){
                    return "Error: connection problem";
                }
            }
        }
        sqlHelper.clearTable("cart");
        return "The payment has been successfully completed";
    }
    public void openConnectionToDB(){
        if (!sqlHelper.connectionIsRun())
            sqlHelper.run();
    }

    public void closeConnectionToDB(){
        if (sqlHelper.connectionIsRun())
            sqlHelper.stop();
    }
}
