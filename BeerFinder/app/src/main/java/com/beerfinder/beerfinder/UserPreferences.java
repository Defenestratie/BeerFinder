package com.beerfinder.beerfinder;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Florian on 8-6-2015.
 */
public class UserPreferences extends PreferenceActivity {
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_preferences);

        settings = PreferenceManager.getDefaultSharedPreferences(UserPreferences.this);
        String radius = settings.getString("beerRadius", null);
        Set<String> beerBrands = settings.getStringSet("beerBrands", null);
        Set<String> beerTypes = settings.getStringSet("beerTypes", null);
        Set<String> establishments = settings.getStringSet("establishments", null);

        JsonToDatabase.setRadius(radius);
        JsonToDatabase.setEstablishments(establishments);
        //TODO database ophalen?

        Log.d("Tag", "Radius: " + radius);
        Log.d("Tag", "Brands: " + beerBrands);
        Log.d("Tag", "Types: " + beerTypes);
        Log.d("Tag", "Establishments: " + establishments);

    }
}
