package ru.mirea.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StuffController {
    @RequestMapping(value = "/stuff", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode stuffs(){
        return StuffService.getStuffs();
    }

    @RequestMapping(value = "/cart/currency/balance", method = RequestMethod.POST)
    @ResponseBody
    public String pay(){
        return StuffService.payTheCart();
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode cart(){
        return StuffService.getCart();
    }

    @RequestMapping(value="/cart/stuff/{id}", method=RequestMethod.DELETE)
    @ResponseBody
    public String deleteStuff(@PathVariable("id") int id){
        return StuffService.deleteStuffFromCart(id);
    }

    @RequestMapping(value = "/cart/stuff/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String putStuff(@PathVariable("id") int id){
        return StuffService.putStuffToCart(id);
    }
}
