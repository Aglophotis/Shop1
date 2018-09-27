package ru.mirea.data.controllers;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.services.StuffService;

@Controller
public class StuffController {

    @Autowired
    private StuffService stuffService;

    @RequestMapping(value = "/stuff", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode getStuffs(){
        return stuffService.getStuffs();
    }

    @RequestMapping(value="/stuff/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ObjectNode getStuff(@PathVariable("id") int id){
        return stuffService.getStuff(id);
    }

}
