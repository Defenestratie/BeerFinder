package com.beerfinder.beerfinder;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Elize on 5-6-2015.
 */
public class Location {
    private String type= null;
    private String name = null;
    private String ID = null;
    private double lat;
    private double lon;
    private String open_now = "Onbekend";
    private String adres =  null;
    private Bitmap icon = null;

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

    public Location(String ID,String name, double lat, double lon, String type, String open_now, String adres ) {
        this.type = type;
        this.name = name;
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
        this.open_now = open_now;
        this.adres = adres;
    }

    public Location(String ID,String name, double lat, double lon, String type, String open_now, String adres, Bitmap icon) {
        this.type = type;
        this.name = name;
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
        this.open_now = open_now;
        this.adres = adres;
        this.icon = icon;
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

    public BitmapDescriptor getTypeIcon(Context context) {
        String imageName = this.getType();
        int imageID = context.getResources().getIdentifier(this.getType() , "drawable", context.getPackageName());
        return BitmapDescriptorFactory.fromResource(imageID);
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }
}
