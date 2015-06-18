package com.beerfinder.beerfinder;

/**
 * Created by Elize on 5-6-2015.
 */
public class Location {
    private String type= null;
    private String name = null;
    private String ID = null;
    private double lat;
    private double lon;

//    public Location(String type, String name, String ID, String adress, String phoneNumber, String website){
//        this.type = type;
//        this.name = name;
//        this.ID = ID;
//        this.adress = adress;
//        this.phoneNumber = phoneNumber;
//        this.website = website;
//
//
//    }

    @Override
    public String toString() {
        return name;
    }

    public Location(String ID, String name, double lat, double lon){
        this.ID = ID;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public Location(String ID,String name, double lat, double lon, String type) {
        this.type = type;
        this.name = name;
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
