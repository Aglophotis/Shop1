package ru.mirea.data.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.shop.entities.Currency;
import ru.mirea.data.shop.services.CurrencyService;

import java.util.List;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(value = "/currency", method = RequestMethod.GET)
    @ResponseBody
    public List<Currency> getCurrencies(){
        return currencyService.getCurrencies();
    }

    @RequestMapping(value="/currency/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Currency getCurrency(@PathVariable("id") int id){
        return currencyService.getCurrency(id);
    }
}
