package ru.mirea.data;

public class Stuff {
    private int id;
    private int price;
    private int count;
    private String name;

    Stuff(int id, int price, int count, String name){
        this.id = id;
        this.price = price;
        this.count = count;
        this.name =  name;
    }

    public void putStuff(int num){
        count += num;
    }

    public void removeStuff(int num){
        count -= num;
    }

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
