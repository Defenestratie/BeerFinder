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
 * Created by Hans on 5-6-2015.
 */
public class JsonToDatabase {

        private static Database database = new Database();
        public static ArrayList<Location> arrayListLocations = new ArrayList<Location>();
        private static String[] typesArray = {"bar" , "cafe" , "liquor_store" , "grocery_or_supermarket"};

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

    public static JSONObject readJsons(String lat, String lon) throws JSONException, IOException {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + lat + "," + lon +
                "&radius=750&types=bar|cafe|liquor_store|grocery_or_supermarket&key=AIzaSyBYQQXbvi7sxgOS7N--8kskwjD6x4pJ73c";

        JSONObject json = readJsonFromUrl(url);
                 Log.i("Tag", url);
        return json;
    }

    public static ArrayList<Location> readJsonInfo(String lat, String lon){
        try {
            JSONObject jsonObject = readJsons(lat, lon);
            if(jsonObject.equals(null)){
                Log.i("Tag", "Json is leeg");

            }
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i = 0; i < jsonArray.length(); i++) {

                //place id
                String placeID = jsonArray.getJSONObject(i).get("place_id").toString();

                //name of place
                String name = jsonArray.getJSONObject(i).get("name").toString();
                //latutude of the place
//                double latitudeLocation =
//                        Double.parseDouble(jsonArray.getJSONObject(i).
//                                getJSONObject("geometry")
//                                .getJSONObject("location")
//                                .get("lat").toString());
//                //longitude of the place
//                double longitudeLocation =
//                        Double.parseDouble(jsonArray.getJSONObject(i).
//                                getJSONObject("geometry")
//                                .getJSONObject("location")
//                                .get("lng").toString());
//
//                //type of the place
//                JSONArray typesInJson = jsonArray.getJSONObject(i).getJSONArray("types");
//                String type = null;
//                for(String t:typesArray){
//                    for(int i2 = 0; i < typesInJson.length(); i++)
//                    if(t.equals(t.equals(typesInJson.get(i2)))){
//                        type = t;
//                        break;
//                    }
//                }
//                //get if location is open right now
//                String open_now = "Onbekend";
//                String open_nowJson = jsonArray.getJSONObject(i).getJSONObject("opening_hours").get("open_now").toString();
//                if(open_nowJson.equals("true")){
//                    open_now = "Open";
//                }else if(open_nowJson.equals("false")){
//                    open_now = "Gesloten";
//                }
//
//                //the adress of the location
//                String adres = jsonArray.getJSONObject(i).get("vicinity").toString();

                //Location location = new Location(placeID, name, latitudeLocation, longitudeLocation, type, open_now, adres);
                Location location = new Location(placeID, name, 0, 0, null, null, null);
                Log.i("Tag", name);
                arrayListLocations.add(location);
                //database.insertLocationIntoDatabase(location);
            }

        }catch(JSONException ex){
            Log.i("", "JSONException..." + ex.getMessage());

        }catch(IOException ex){
            Log.i("", "IOException...");

        }catch(Exception ex){
            Log.i("Tag", "Er is iets mis gegaan " + ex.getMessage());


        }finally{
            database.closeDatabase();
            waitForList();
            return arrayListLocations;
        }

    }

    private static void waitForList() {
        if(arrayListLocations.isEmpty()){
            try {
                Thread.sleep(500);

            }catch(InterruptedException ex){
                Log.i("Tag", "Interupted..");

            }
        }
    }


}
