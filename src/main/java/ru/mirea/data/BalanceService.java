package ru.mirea.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    private SQLHelper sqlHelper;

    public ObjectNode getBalance() {
        return sqlHelper.selectAllFromBalance(1);
    }

    public String increaseBalance(int id_currency, double value) {
        double exchangeRate = sqlHelper.selectColumnValueById("exchange_rate", "currency", id_currency);
        if (exchangeRate == -1) {
            return "Error: connection problems";
        }
        double currentBalance = sqlHelper.selectBalanceByCurrencyID(id_currency, 1);
        if (currentBalance == -1) {
            return "Error: connection problems";
        }
        if (sqlHelper.updateBalanceByCurrencyID(id_currency, 1, currentBalance + value) == -1) {
            return "Error: connection problems";
        }
        return "Balance successfully updated";
    }

    public String decreaseBalance(int id_currency, double value) {
        double exchangeRate = sqlHelper.selectColumnValueById("exchange_rate", "currency", id_currency);
        if (exchangeRate == -1) {
            return "Error: connection problems";
        }
        double currentBalance = sqlHelper.selectBalanceByCurrencyID(id_currency, 1);
        if (currentBalance == -1) {
            return "Error: connection problems";
        }
        if (currentBalance == 0){
            return "Your balance is zero";
        }
        if (currentBalance < value){
            return "Error: your balance is below the amount withdrawn";
        }
        if (sqlHelper.updateBalanceByCurrencyID(id_currency, 1, currentBalance - value) == -1) {
            return "Error: connection problems";
        }
        return "Balance successfully updated";
    }
}
