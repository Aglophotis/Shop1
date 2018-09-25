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
    public ObjectNode cart(){
        return cartService.getCart();
    }

    @RequestMapping(value="/cart/stuff/{id}", method=RequestMethod.DELETE)
    @ResponseBody
    public String deleteStuff(@PathVariable("id") int id){
        return cartService.deleteStuffFromCart(id);
    }

    @RequestMapping(value = "/cart/stuff/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String putStuff(@PathVariable("id") int id){
        return cartService.putStuffToCart(id);
    }
}
