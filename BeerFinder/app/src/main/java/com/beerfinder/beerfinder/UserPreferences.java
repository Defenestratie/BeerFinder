package com.beerfinder.beerfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Set;

/**
 * Created by Florian on 8-6-2015.
 */
public class UserPreferences extends PreferenceActivity {
    private static String radius;
    private static Set<String> beerBrands;
    private static Set<String> beerTypes;
    private static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_preferences);


    }

    public static void checkPreferences(Context context){
        try {
            settings = PreferenceManager.getDefaultSharedPreferences(context);

            radius = settings.getString("beerRadius", null);
            beerBrands = settings.getStringSet("beerBrands", null);
            beerTypes = settings.getStringSet("beerTypes", null);
            Set<String> establishments = settings.getStringSet("establishments", null);

            JsonToDatabase.setRadius(radius);
            JsonToDatabase.setEstablishments(establishments);

            Log.d("Tag", "Radius: " + radius);
            Log.d("Tag", "Brands: " + beerBrands);
            Log.d("Tag", "Types: " + beerTypes);
            Log.d("Tag", "Establishments: " + establishments);
        }catch(Exception ex){
            Log.d("Tag", "Alles is kapot" + ex.getMessage());
        }finally {
            Log.d("Tag", "Door UserPreference methode ding gegaan");
        }
    }

    public static Set<String> getBeerBrands() {
        return beerBrands;
    }

    public static Set<String> getBeerTypes() {
        return beerTypes;
    }
}
