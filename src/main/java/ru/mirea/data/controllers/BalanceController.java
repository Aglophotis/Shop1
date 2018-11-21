package ru.mirea.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mirea.data.entities.Balance;
import ru.mirea.data.services.BalanceService;

import java.util.List;

@Controller
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    @ResponseBody
    public List<Balance> getBalance(){
        return balanceService.getBalance();
    }

    @RequestMapping(value="/currency/{id}/balance/{value}", method=RequestMethod.PUT)
    @ResponseBody
    public String putBalance(@PathVariable("id") int id, @PathVariable("value") double value){
        return balanceService.increaseBalance(id, value);
    }

    @RequestMapping(value="/currency/{id}/balance/{value}", method=RequestMethod.DELETE)
    @ResponseBody
    public String deleteBalance(@PathVariable("id") int id, @PathVariable("value") double value){
        return balanceService.decreaseBalance(id, value);
    }
}
