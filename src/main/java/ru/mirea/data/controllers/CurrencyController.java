package ru.mirea.data.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.services.CurrencyService;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(value = "/currency", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode getCurrencies(){
        return currencyService.getCurrencies();
    }

    @RequestMapping(value="/currency/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ObjectNode getCurrency(@PathVariable("id") int id){
        return currencyService.getCurrency(id);
    }
}
