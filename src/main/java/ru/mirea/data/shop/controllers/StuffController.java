package ru.mirea.data.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.shop.entities.Item;
import ru.mirea.data.shop.services.StuffService;

import java.util.List;

@Controller
public class StuffController {

    @Autowired
    private StuffService stuffService;

    @RequestMapping(value = "/stuff", method = RequestMethod.GET)
    @ResponseBody
    public List<Item> getStuffs(){
        return stuffService.getStuffs();
    }

    @RequestMapping(value="/stuff/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Item getStuff(@PathVariable("id") int id){
        return stuffService.getStuff(id);
    }

}
