package ru.mirea.data.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.entities.Item;
import ru.mirea.data.services.PetService;

import java.util.List;

@Controller
public class PetController {

    @Autowired
    private PetService petService;

    @RequestMapping(value = "/pet", method = RequestMethod.GET)
    @ResponseBody
    public List<Item> getPets(){
        return petService.getPets();
    }

    @RequestMapping(value="/pet/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Item getStuff(@PathVariable("id") int id){
        return petService.getPet(id);
    }
}
