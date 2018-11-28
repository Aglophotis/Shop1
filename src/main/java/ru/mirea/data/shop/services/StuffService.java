package ru.mirea.data.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.shop.entities.Item;
import ru.mirea.data.shop.repository.ItemRepository;

import java.util.List;

@Service
public class StuffService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getStuffs(){
        return itemRepository.getAllStuffs();
    }

    public Item getStuff(int id){
        return itemRepository.getStuffById(id);
    }


}
