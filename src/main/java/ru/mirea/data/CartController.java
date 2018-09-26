package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode getCart(){
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

    @RequestMapping(value = "/cart/payment", method = RequestMethod.POST)
    @ResponseBody
    public String pay() {
        return cartService.paymentOfCart();
    }
}
