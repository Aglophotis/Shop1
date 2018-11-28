package ru.mirea.data.shop.entities;

public class Currency {
    private int id;
    private String currency;
    private double exchangeRate;

    public Currency(int id, String currency, double exchangeRate){
        this.id = id;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    public int getId() {
        return id;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
