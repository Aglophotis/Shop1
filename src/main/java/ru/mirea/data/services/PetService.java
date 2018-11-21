package ru.mirea.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.entities.Item;
import ru.mirea.data.dao.ItemDao;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private ItemDao itemDao;

    public List<Item> getPets(){
        return itemDao.getAllPets();
    }

    public Item getPet(int id){
        return itemDao.getPetById(id);
    }

}
