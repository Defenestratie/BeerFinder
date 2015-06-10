package com.beerfinder.beerfinder;

<<<<<<< HEAD
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
=======
import android.content.Context;
>>>>>>> origin/master
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

<<<<<<< HEAD
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

=======
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
>>>>>>> origin/master


public class MainActivity
        extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        placePicker();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
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

<<<<<<< HEAD
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void openMap(View v)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
=======
    public void placePicker() {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
>>>>>>> origin/master

        try {
            Context context = getApplicationContext();
            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
        }
        catch (Exception e) {

        }
    }

    @Override
    public void onConnected(Bundle bundle) {

<<<<<<< HEAD
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


=======
    }
>>>>>>> origin/master

    @Override
    public void onConnectionSuspended(int i) {

<<<<<<< HEAD
        //Toast.makeText(getApplicationContext(),"Radius " + radius, Toast.LENGTH_SHORT);
=======
    }
>>>>>>> origin/master

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
