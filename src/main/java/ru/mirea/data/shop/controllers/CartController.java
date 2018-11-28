package ru.mirea.data.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.shop.entities.CartItem;
import ru.mirea.data.shop.services.CartService;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public List<CartItem> getCart(){
        return cartService.getCart();
    }

    @RequestMapping(value="/cart/{id}", method=RequestMethod.DELETE)
    @ResponseBody
    public String deleteItem(@PathVariable("id") int id){
        return cartService.deleteItemFromCart(id);
    }

    @RequestMapping(value = "/cart/item/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String putItem(@PathVariable("id") int id){
        return cartService.putItemToCart(id);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    @ResponseBody
    public String pay() {
        return cartService.paymentOfCart();
    }
}
