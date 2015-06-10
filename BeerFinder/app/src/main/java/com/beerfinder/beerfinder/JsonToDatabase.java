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


/**
 * Created by Hans on 5-6-2015.
 */
public class JsonToDatabase {

        private static Database database = new Database();

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
        JSONObject json = readJsonFromUrl(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                        + lat + "," + lon +
                        "&radius=750&types=bar|cafe|liquor_store|grocery_or_supermarket&key=AIzaSyBYQQXbvi7sxgOS7N--8kskwjD6x4pJ73c");

        return json;
    }

    public static void readJsonInfo(String lat, String lon){
        try {
            JSONObject jsonObject = readJsons(lat, lon);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i = 0; i < jsonArray.length(); i++) {
                String placeID = jsonArray.getJSONObject(i).get("place_id").toString();
                String name = jsonArray.getJSONObject(i).get("name").toString();
                Location location = new Location(placeID, name);
                database.insertLocationIntoDatabase(location);
            }

        }catch(JSONException ex){
            Log.i("", "JSONException..." + ex.getMessage());

        }catch(IOException ex){
            Log.i("", "IOException...");

        }finally{
            database.closeDatabase();
        }




    }





}