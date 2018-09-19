package ru.mirea.data;

import java.util.HashMap;

public class StuffService {
    private static HashMap<Integer, Stuff> stuffs = new HashMap<>();
    private static HashMap<Integer, Integer> cart = new HashMap<>();

    public static HashMap<Integer, Stuff> getStuffs(){
        return stuffs;
    }

    public static HashMap<Integer, Integer> getCart(){ return cart;}

    public static String payTheCart(){
        try {
            if (cart.isEmpty())
                return "Cart is empty";
            for (Integer item : cart.keySet()) {
                stuffs.get(item).removeStuff(cart.get(item));
            }
        } catch (Exception e) {
            return e.toString();
        }
        cart.clear();
        return "Payment was successful";
    }

    public static String deleteStuffFromCart(int id){
        try {
            if (cart.get(id) > 1) {
                cart.put(id, cart.get(id) - 1);
            } else {
                cart.remove(id);
            }
        } catch (NullPointerException e){
            return "Error: id wasn't found";
        }
        return "Stuff was been deleted from cart";
    }

    public static String putStuffToCart(int id){
        try{
            if (stuffs.get(id).getCount() != cart.get(id)) {
                System.out.println("TEST1");
                cart.put(id, cart.get(id) + 1);
            } else {
                return "Out of stock stuffs";
            }
        } catch (NullPointerException e){
            try {
                stuffs.get(id).getCount();
            } catch (NullPointerException ex){
                return "Error: id wasn't found";
            }
            cart.put(id, 1);
            return "Stuff was been added to cart";
        }
        return "Stuff was been added to cart";
    }

    public static void createStuffsList(){
        stuffs.put(0, new Stuff(0, 100, 4, "Ball"));
        stuffs.put(1, new Stuff(1, 70, 20, "Milk"));
        stuffs.put(2, new Stuff(2, 400, 1, "Bone"));
        stuffs.put(3, new Stuff(3, 100, 2, "Dog-collar"));
        stuffs.put(4, new Stuff(4, 120, 1, "Feed"));
    }
}
