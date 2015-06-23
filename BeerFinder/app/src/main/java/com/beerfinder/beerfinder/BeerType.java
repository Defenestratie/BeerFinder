package com.beerfinder.beerfinder;

/**
 * Created by School on 23-6-2015.
 */
public class BeerType {

    int ID = 0;
    String name = null;

    public BeerType(int ID, String name){
        this.ID = ID;
        this.name =  name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getID() {
        return ID;
    }
}
