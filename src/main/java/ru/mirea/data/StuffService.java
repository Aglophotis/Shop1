package ru.mirea.data;

import java.util.ArrayList;

public class StuffService {
    static ArrayList<Stuff> stuffs;

    public static void main(String[] args){
        stuffs = createStuffsList();
    }

    public static ArrayList<Stuff> getStuffs(){
        return stuffs;
    }

    public static void delete(int id){
        return;
    }

    private static ArrayList<Stuff> createStuffsList(){
        ArrayList<Stuff> stuffs = new ArrayList<>();
        stuffs.add(new Stuff(0, 100, 4, "Ball"));
        stuffs.add(new Stuff(1, 70, 20, "Milk"));
        stuffs.add(new Stuff(2, 400, 1, "Bone"));
        stuffs.add(new Stuff(3, 100, 2, "Dog-collar"));
        stuffs.add(new Stuff(4, 120, 1, "Feed"));
        return stuffs;
    }
}
