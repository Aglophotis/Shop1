package ru.mirea.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.entities.Item;
import ru.mirea.data.dao.ItemDao;

import java.util.List;

@Service
public class StuffService {

    @Autowired
    private ItemDao itemDao;

    public List<Item> getStuffs(){
        return itemDao.getAllStuffs();
    }

    public Item getStuff(int id){
        return itemDao.getStuffById(id);
    }


}
