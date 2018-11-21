package ru.mirea.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.entities.Currency;
import ru.mirea.data.dao.CurrencyDao;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyDao currencyDao;

    public List<Currency> getCurrencies(){
        return currencyDao.getAllCurrencies();
    }

    public Currency getCurrency(int id){
        return currencyDao.getCurrencyById(id);
    }
}
