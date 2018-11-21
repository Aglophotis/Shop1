package ru.mirea.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.entities.Balance;
import ru.mirea.data.entities.Currency;
import ru.mirea.data.dao.BalanceDao;
import ru.mirea.data.dao.CurrencyDao;

import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private BalanceDao balanceDao;

    public List<Balance> getBalance() {
        return balanceDao.getBalances();
    }

    public String increaseBalance(int id_currency, double value) {
        Currency currency = currencyDao.getCurrencyById(id_currency);
        if (currency == null) {
            return "Error: connection problems";
        }
        Balance balance = balanceDao.getBalanceByCurrencyId(id_currency);
        if (balance == null) {
            return "Error: connection problems";
        }
        if (balanceDao.updateBalanceByCurrencyID(id_currency, balance.getBalance() + value) == -1) {
            return "Error: connection problems";
        }
        return "Balance successfully updated";
    }

    public String decreaseBalance(int id_currency, double value) {
        Currency currency = currencyDao.getCurrencyById(id_currency);
        if (currency == null) {
            return "Error: connection problems";
        }
        Balance balance = balanceDao.getBalanceByCurrencyId(id_currency);
        if (balance == null) {
            return "Error: connection problems";
        }
        if (balance.getBalance() == 0){
            return "Your balance is zero";
        }
        if (balance.getBalance() < value){
            return "Error: your balance is below the amount withdrawn";
        }
        if (balanceDao.updateBalanceByCurrencyID(id_currency, balance.getBalance() - value) == -1) {
            return "Error: connection problems";
        }
        return "Balance successfully updated";
    }
}
