package ru.mirea.data.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.SQLHelper;

@Service
public class CurrencyService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getCurrencies(){
        return sqlHelper.selectAllFromCurrency();
    }

    public ObjectNode getCurrency(int id){
        return sqlHelper.selectConcreteFromCurrencyByID(id);
    }
}
