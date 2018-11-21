package ru.mirea.data.entities;

public class CartItem {
    private int id;
    private int idAuthor;
    private int idItem;

    public CartItem(int id, int idItem, int idAuthor){
        this.id = id;
        this.idAuthor = idAuthor;
        this.idItem = idItem;
    }

    public int getId() {
        return id;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }
}
