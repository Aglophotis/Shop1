package ru.mirea.data.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.shop.entities.Currency;
import ru.mirea.data.shop.repository.CurrencyRepository;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> getCurrencies(){
        return currencyRepository.getAllCurrencies();
    }

    public Currency getCurrency(int id){
        return currencyRepository.getCurrencyById(id);
    }
}
