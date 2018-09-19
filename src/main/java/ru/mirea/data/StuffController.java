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
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode stuffsArray = mapper.createArrayNode();
        for (int i = 0; i < StuffService.getStuffs().size(); i++){
            Stuff item = StuffService.getStuffs().get(i);
            ObjectNode stuff = mapper.createObjectNode();
            stuff.put("id", item.getId());
            stuff.put("name", item.getName());
            stuff.put("price", item.getPrice());
            stuff.put("count", item.getCount());
            stuffsArray.add(stuff);
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("stuffs", stuffsArray);
        return objectNode;
    }

    @RequestMapping(value = "/cart/currency/balance", method = RequestMethod.POST)
    @ResponseBody
    public String pay(){
        return StuffService.payTheCart();
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode cart(){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode stuffsArray = mapper.createArrayNode();
        for (Integer item : StuffService.getCart().keySet()){
            ObjectNode stuff = mapper.createObjectNode();
            stuff.put("id", item);
            stuff.put("count", StuffService.getCart().get(item));
            stuffsArray.add(stuff);
        }
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("cart", stuffsArray);
        return objectNode;
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
