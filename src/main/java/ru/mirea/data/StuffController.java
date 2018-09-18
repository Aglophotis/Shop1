package ru.mirea.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class StuffController {
    @RequestMapping(value="/pet", method = RequestMethod.GET)
    @ResponseBody
    public String pets(){
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode = mapper.createArrayNode();

        ArrayNode stuffsArray = mapper.createArrayNode();
        StuffService.createStuffsList();
        for (Stuff item : createStuffsList()){
            ObjectNode stuff = mapper.createObjectNode();
            stuff.put("id", item.getId());
            stuff.put("name", item.getName());
            stuff.put("price", item.getPrice());
            stuff.put("count", item.getCount());
            stuffsArray.add(stuff);
        }

        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO("stuffs", stuffsArray);

        return objectNode.toString();
    }

    private ArrayList<Stuff> createStuffsList(){
        ArrayList<Stuff> stuffs = new ArrayList<>();
        stuffs.add(new Stuff(0, 100, 4, "Ball"));
        stuffs.add(new Stuff(1, 70, 20, "Milk"));
        stuffs.add(new Stuff(2, 400, 1, "Bone"));
        stuffs.add(new Stuff(3, 100, 2, "Dog-collar"));
        stuffs.add(new Stuff(4, 120, 1, "Feed"));
        return stuffs;
    }

    @RequestMapping(value="/pet/[id]", method=RequestMethod.DELETE)
    public void pets(int id){
        StuffService.delete(id);
    }
}
