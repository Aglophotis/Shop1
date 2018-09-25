package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getCart(){
        return sqlHelper.selectAllFromCart();
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

}
