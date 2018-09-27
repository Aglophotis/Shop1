package ru.mirea.data.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.SQLHelper;

@Service
public class PetService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getPets(){
        return sqlHelper.selectAllFromItems("Pet");
    }

    public ObjectNode getPet(int id){
        return sqlHelper.selectConcreteFromItemsById("Pet", id);
    }

}
