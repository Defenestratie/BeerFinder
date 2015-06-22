package com.beerfinder.beerfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import java.util.concurrent.ExecutionException;

/**
 * Created by Florian on 21-6-2015.
 */
public class Splash extends Activity {
    private final int SPLASH_DURATION = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent splashIntent = new Intent(Splash.this, MapActivity.class);
//                Splash.this.startActivity(splashIntent);
//                Splash.this.finish();
//            }
//        }, SPLASH_DURATION);

        try {

            getUserLocation();
            //Json call
            MapActivity.setJsonObject();
            Log.d("Tag", "Json opgehaald");
            //Json uitpakken
            MapActivity.getLocationList();
            Log.d("Tag", "List opgehaald.");
            //Verbinden met database
            new Database().execute().get();
            Log.d("Tag", "Database verbinden");
            Intent splashIntent = new Intent(Splash.this, MapActivity.class);
            startActivity(splashIntent);
            //this.finish();

        } catch (ExecutionException ex) {
            Log.i("Tag", "Database ophalen onderbroken!");

        } catch (InterruptedException ex) {
            Log.i("Tag", "Database ophalen onderbroken!");
        }

    }


    private void getUserLocation(){

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        android.location.Location myLocation = locationManager.getLastKnownLocation(provider);

        Log.d("Location provider", "" + locationManager.getAllProviders().size());

        // Get latitude of the current location
        MapActivity.myLatitude = myLocation.getLatitude();

        // Get longitude of the current location
        MapActivity.myLongitude = myLocation.getLongitude();
    }

}
