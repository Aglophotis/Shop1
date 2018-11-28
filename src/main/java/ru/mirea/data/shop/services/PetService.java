package ru.mirea.data.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.shop.entities.Item;
import ru.mirea.data.shop.repository.ItemRepository;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getPets(){
        return itemRepository.getAllPets();
    }

    public Item getPet(int id){
        return itemRepository.getPetById(id);
    }

}
