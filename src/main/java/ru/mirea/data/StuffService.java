package ru.mirea.data;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;

public class StuffService {
    private static HashMap<Integer, Integer> cart = new HashMap<>();
    private static SQLWorker sqlWorker;

    public static ObjectNode getStuffs(){
        return sqlWorker.selectAll();
    }

    public static ObjectNode getCart(){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode stuffsArray = mapper.createArrayNode();
        for (Integer id : cart.keySet()){
            ObjectNode stuff = mapper.createObjectNode();
            stuff.put("id", id);
            stuff.put("count", cart.get(id));
            stuffsArray.add(stuff);
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("cart", stuffsArray);
        return objectNode;
    }

    public static String payTheCart(){
        try{
            if (cart.isEmpty()) return "Cart is empty";
            for (Integer id : cart.keySet()){
                int count = sqlWorker.selectCount(id);
                int cartCount = cart.get(id);
                if (cartCount > count)
                    return "Unfortunately, while you were buying, part of the stuffs were sold out";
                if (sqlWorker.updateCount(id, count - cartCount) == -1)
                    return "Error: the connection to the database was broken";
            }
        } catch (Exception e){
            return "Unknown error";
        }
        cart.clear();
        return "Payment was successful";
    }

    public static String deleteStuffFromCart(int id){
        try {
            if (cart.get(id) > 1)
                cart.put(id, cart.get(id) - 1);
            else
                cart.remove(id);
        } catch (NullPointerException e){
            return "Error: id wasn't found";
        }
        return "Stuff was been deleted from cart";
    }

    public static String putStuffToCart(int id){
        int count = sqlWorker.selectCount(id);
        if (count == -1) return "Error: id wasn't found";
        if (count == 0) return "The stuffs are over";
        try{
            if (cart.get(id) != count)
                cart.put(id, cart.get(id) + 1);
            else
                return "The stuffs are over";
        } catch (NullPointerException e){
            cart.put(id, 1);
            return "Stuff was been added to cart";
        }
        return "Stuff was been added to cart";
    }


    public static void openConnToBD(){
        sqlWorker= new SQLWorker();
        sqlWorker.run();
    }
}
