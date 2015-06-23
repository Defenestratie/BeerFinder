package com.beerfinder.beerfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by Florian on 21-6-2015.
 */
public class Splash extends Activity implements LocationListener {
    private final int SPLASH_DURATION = 2000;
    LocationManager locationmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("Tag", "Starting task...");
                    gatherData();

                } finally {
                    Intent intent = new Intent(Splash.this, MapActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, SPLASH_DURATION);

    }

    private void gatherData(){
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getUserLocation();
                }
            });
            thread.start();
            thread.join();

            while(MapActivity.myLongitude == 0.0 && MapActivity.myLatitude == 0.0){
                Toast.makeText(getApplicationContext(), "Locatie Ophalen...", Toast.LENGTH_LONG);
                Thread.sleep(1000);
            }

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


    }

    private void getUserLocation() {
//
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        // Create a criteria object to retrieve provider
//        Criteria criteria = new Criteria();
//
//        // Get the name of the best provider
//        String provider = locationManager.getBestProvider(criteria, true);
//
//        // Get Current Location
//        android.location.Location myLocation = locationManager.getLastKnownLocation(provider);
//
//        Log.d("Location provider", "" + locationManager.getAllProviders().size());
//
//
//        // Get latitude of the current location
//        MapActivity.myLatitude = myLocation.getLatitude();
//
//        // Get longitude of the current location
//        MapActivity.myLongitude = myLocation.getLongitude();


            locationmanager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria =new Criteria();

            String provider=locationmanager.getBestProvider(criteria, true);

            if(provider != null & !provider.equals(""))

            {

                Location location = locationmanager.getLastKnownLocation(provider);

                locationmanager.requestLocationUpdates(provider,2000,1,this);

                if(location != null)

                {

                    onLocationChanged(location);

                }

                else{

                    Toast.makeText(getApplicationContext(),"location not found", Toast.LENGTH_LONG ).show();

                }

            }

            else

            {

                Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();

            }
    }


        @Override
        public void onLocationChanged(Location location) {
            // Get latitude of the current location
            MapActivity.myLatitude = location.getLatitude();

            // Get longitude of the current location
            MapActivity.myLongitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


