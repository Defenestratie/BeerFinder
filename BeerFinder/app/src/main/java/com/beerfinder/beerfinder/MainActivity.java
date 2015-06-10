package com.beerfinder.beerfinder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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




        //Toast.makeText(getApplicationContext(),"Radius " + radius, Toast.LENGTH_SHORT);


//        Intent map =  new Intent(this, MapActivity.class);
//        startActivity(map);
    }
}
