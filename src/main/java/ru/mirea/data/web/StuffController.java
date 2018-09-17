package ru.mirea.data;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class StuffController {
    //@RequestMapping(value="/pet", method = RequestMethod.GET)
    //@ResponseBody
    public ArrayList<Stuff> pets(){
        return StuffService.getStuffs();
    }

    @RequestMapping(value="/pet")
    public String printHello(ModelMap modelMap){
        modelMap.addAttribute("message", "Hello Spring MVC Framework!");
        System.out.println("Hello");
        return "Hello";
    }

    @RequestMapping(value="/pet/[id]", method=RequestMethod.DELETE)
    public void pets(int id){
        StuffService.delete(id);
    }
}
