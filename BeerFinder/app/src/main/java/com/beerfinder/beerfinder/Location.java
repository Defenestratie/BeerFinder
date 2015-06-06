package com.beerfinder.beerfinder;

/**
 * Created by School on 5-6-2015.
 */
public class Location {
    private String type= null;
    private String name = null;
    private String ID = null;
    private String adress = null;
    private String phoneNumber = null;
    private String website = null;

    public Location(String type, String name, String ID, String adress, String phoneNumber, String website){
        this.type = type;
        this.name = name;
        this.ID = ID;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.website = website;


    }

    public Location(String ID, String name){
        this.ID = ID;
        this.name = name;


    }


    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
