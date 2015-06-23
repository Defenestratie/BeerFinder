package com.beerfinder.beerfinder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity implements OnMapReadyCallback {

    //private GoogleApiClient mGoogleApiClient;
    Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void openMap(View v)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String radius = sp.getString("beerRadius","");

        switch(radius)
        {
            case "1" : radius = "1";break;
            case "2" : radius = "2";break;
            case "3" : radius = "5";break;
            case "4" : radius = "10";break;
            case "5" : radius = "25";break;

        }

        Set<String> set = new HashSet<String>();

        set = sp.getStringSet("beerBrands", set);

        for (String s : set) {
            Log.d("beerBrands", s);
        }


        Toast.makeText(getApplicationContext(), "Radius " + radius, Toast.LENGTH_SHORT);
        db.insertBeerIntoDatabase("test", "normaal", 3);

    }

    public void insertDatabase(View v)
    {



    }

//    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//    for (int z = 0; z < list.size(); z++) {
//        LatLng point = list.get(z);
//        options.add(point);
//    }
//    line = myMap.addPolyline(options);
}
