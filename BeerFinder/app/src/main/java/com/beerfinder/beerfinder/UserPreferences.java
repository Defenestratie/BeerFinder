package com.beerfinder.beerfinder;

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

    private static Set<String> beerBrands;
    private static Set<String> beerTypes;

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_preferences);

        settings = PreferenceManager.getDefaultSharedPreferences(UserPreferences.this);
        String radius = settings.getString("beerRadius", null);
        beerBrands = settings.getStringSet("beerBrands", null);
        beerTypes = settings.getStringSet("beerTypes", null);
        Set<String> establishments = settings.getStringSet("establishments", null);

        JsonToDatabase.setRadius(radius);
        JsonToDatabase.setEstablishments(establishments);
        //TODO database ophalen?

        Log.d("Tag", "Radius: " + radius);
        Log.d("Tag", "Brands: " + beerBrands);
        Log.d("Tag", "Types: " + beerTypes);
        Log.d("Tag", "Establishments: " + establishments);

    }

    public static Set<String> getBeerBrands() {
        return beerBrands;
    }

    public static Set<String> getBeerTypes() {
        return beerTypes;
    }
}
