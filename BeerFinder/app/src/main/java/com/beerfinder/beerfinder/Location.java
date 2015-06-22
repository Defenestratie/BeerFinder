package com.beerfinder.beerfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    private String address =  null;
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

    public Location(String ID,String name, double lat, double lon, String type, String open_now, String adres) {
        this.type = type;
        this.name = name;
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
        this.open_now = open_now;
        this.address = adres;
    }

    public Location(String ID,String name, double lat, double lon, String type, String open_now, String address, Bitmap icon) {
        this.type = type;
        this.name = name;
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
        this.open_now = open_now;
        this.address = address;
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

    public String getAddress()
    {
        return address;
    }

    public BitmapDescriptor getTypeIcon(Context context) {
        int imageID = context.getResources().getIdentifier(this.getType() , "drawable", context.getPackageName());
        return BitmapDescriptorFactory.fromResource(imageID);
    }

    public Bitmap getListTypeIcon(Context context){
        int imageID = context.getResources().getIdentifier(this.getType() + "list" , "drawable", context.getPackageName());
        Bitmap iconForList = BitmapFactory.decodeResource(context.getResources(),
                imageID);

        return iconForList;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }
}
