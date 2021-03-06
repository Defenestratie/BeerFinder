package com.beerfinder.beerfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;

import java.util.concurrent.ExecutionException;

/**
 * Created by Florian on 21-6-2015.
 */
public class Splash extends Activity {
    private final int SPLASH_DURATION = 2000;
    LocationManager locationmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("Tag", "Starting task...");
                    gatherData();
                } catch (Exception ex) {
                    new AlertDialog.Builder(Splash.this)
                            .setTitle("Error")
                            .setMessage("Er is iets mis gegaan met het ophalen van uw locatie of de informatie uit de database." +
                                    "Controleer uw internetverbinding.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   System.exit(0);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } finally {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, SPLASH_DURATION);

    }

    private void gatherData() {
        try {
            new getUserData().execute().get();
            //UserPreferences ophalen
            UserPreferences.checkPreferences(this);
            Log.d("Tag", "UserPreferences opgehaald");
            //json ophalen
            MapActivity.setJsonObject();
            Log.d("Tag", "Json opgehaald");
            //Json uitpakken
            MapActivity.getLocationList1();
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


    private class getUserData extends AsyncTask<Void, Void, Void> implements LocationListener {


        @Override
        protected Void doInBackground(Void... params) {
            Looper.prepare();
            try {
                getUserLocation();
            } catch (Exception ex) {
                Log.d("Tag", "Er is iets mis gegaan bij het ophalen van de locatie");

            }
            return null;
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

        private void getUserLocation() {

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // Get latitude of the current location
            MapActivity.myLatitude = location.getLatitude();

            // Get longitude of the current location
            MapActivity.myLongitude = location.getLongitude();


        }


        @Override
        public void onLocationChanged(Location location) {
            // Get latitude of the current location
            MapActivity.myLatitude = location.getLatitude();

            // Get longitude of the current location
            MapActivity.myLongitude = location.getLongitude();
        }
    }


    }



