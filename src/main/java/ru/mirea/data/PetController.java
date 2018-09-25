package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PetController {

    @Autowired
    private PetService petService;

    @RequestMapping(value = "/pet", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode pets(){
        return petService.getPets();
    }
}
