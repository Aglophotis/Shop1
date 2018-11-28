package ru.mirea.data.shop.entities;

public class Item {
    private int id;
    private String type;
    private String name;
    private int price;
    private int count;

    public Item(int id, String type, String name, int price, int count){
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }
}
