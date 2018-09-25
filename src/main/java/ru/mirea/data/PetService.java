package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getPets(){
        return sqlHelper.selectAllFromItems("Pet");
    }

}
