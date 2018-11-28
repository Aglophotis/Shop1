package ru.mirea.data.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.shop.entities.Balance;
import ru.mirea.data.shop.entities.Currency;
import ru.mirea.data.shop.repository.BalanceRepository;
import ru.mirea.data.shop.repository.CurrencyRepository;

import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    public List<Balance> getBalance() {
        return balanceRepository.getBalances();
    }

    public String increaseBalance(int id_currency, double value) {
        Currency currency = currencyRepository.getCurrencyById(id_currency);
        if (currency == null) {
            return "Error: connection problems";
        }
        Balance balance = balanceRepository.getBalanceByCurrencyId(id_currency);
        if (balance == null) {
            return "Error: connection problems";
        }
        if (balanceRepository.updateBalanceByCurrencyID(id_currency, balance.getBalance() + value) == -1) {
            return "Error: connection problems";
        }
        return "Balance successfully updated";
    }

    public String decreaseBalance(int id_currency, double value) {
        Currency currency = currencyRepository.getCurrencyById(id_currency);
        if (currency == null) {
            return "Error: connection problems";
        }
        Balance balance = balanceRepository.getBalanceByCurrencyId(id_currency);
        if (balance == null) {
            return "Error: connection problems";
        }
        if (balance.getBalance() == 0){
            return "Your balance is zero";
        }
        if (balance.getBalance() < value){
            return "Error: your balance is below the amount withdrawn";
        }
        if (balanceRepository.updateBalanceByCurrencyID(id_currency, balance.getBalance() - value) == -1) {
            return "Error: connection problems";
        }
        return "Balance successfully updated";
    }
}
