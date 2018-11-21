package ru.mirea.data.entities;

public class Balance {
    private int id;
    private int idAuthor;
    private int idCurrency;
    private double balance;

    public Balance(int id, int idAuthor, int idCurrency, double balance){
        this.id = id;
        this.idAuthor = idAuthor;
        this.balance = balance;
        this.idCurrency = idCurrency;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public int getIdCurrency() {
        return idCurrency;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setIdCurrency(int idCurrency) {
        this.idCurrency = idCurrency;
    }
}
