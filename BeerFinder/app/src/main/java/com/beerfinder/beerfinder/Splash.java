package com.beerfinder.beerfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.AsyncTask;
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

        new SplashTask().execute();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent splashIntent = new Intent(Splash.this, MapActivity.class);
//                Splash.this.startActivity(splashIntent);
//                Splash.this.finish();
//            }
//        }, SPLASH_DURATION);

    }




    private void getUserLocation() {

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


    private class SplashTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
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


            } catch (ExecutionException ex) {
                Log.i("Tag", "Database ophalen onderbroken!");

            } catch (InterruptedException ex) {
                Log.i("Tag", "Database ophalen onderbroken!");
            } catch (Exception ex) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                android.location.Location myLocation = locationManager.getLastKnownLocation(provider);


                Log.d("location test", "location =  " + myLocation);

                if (myLocation == null) {

                }
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(Splash.this, MapActivity.class);
            startActivity(intent);
            finish();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
