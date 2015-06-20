package com.beerfinder.beerfinder;

/**
 * Created by Florian on 18/06/2015.
 */
public class LocationPointer {
    private double lat;
    private double lon;

    public LocationPointer(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
