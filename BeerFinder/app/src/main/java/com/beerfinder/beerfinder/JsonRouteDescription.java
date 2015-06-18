package com.beerfinder.beerfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Florian on 18/06/2015.
 */
public class JsonRouteDescription {

    public static ArrayList<LocationPointer> pointersList;

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsons(String lat, String lon, Location location) throws JSONException, IOException {
        JSONObject json = readJsonFromUrl(
                "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        lat + "," + lon + "&destination=" + location.getLat() + "," + location.getLon() +
                        "&mode=walking&key=AIzaSyCczmi8th0UOBpa8dSOtIN2nvqNhaaq9kI");
        return json;
    }

    public static ArrayList<LocationPointer> readJsonInfo(String lat, String lon, Location location){
        try {
            JSONObject jsonObject = readJsons(lat, lon, location);
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            JSONArray legs = jsonArray.getJSONObject(0).getJSONArray("legs");
            JSONArray route1 = legs.getJSONObject(0).getJSONArray("steps");
            pointersList = new ArrayList<LocationPointer>();
            for(int i = 0; i < route1.length(); i++){
                JSONObject object = route1.getJSONObject(i).getJSONObject("end_location");
                double latEndLocation = Double.valueOf(object.get("lat").toString());
                double lonEndLocation = Double.valueOf(object.get("lng").toString());
                LocationPointer pointer = new LocationPointer(latEndLocation, lonEndLocation);
                pointersList.add(pointer);
            }
        }catch(JSONException ex){
            Log.i("", "JSONException..." + ex.getMessage());

        }catch(IOException ex){
            Log.i("", "IOException...");
        }
        return pointersList;
    }
}
