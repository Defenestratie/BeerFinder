package com.beerfinder.beerfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class LocationInfo_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info_activity);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String type = intent.getStringExtra("Type");
        String address =  intent.getStringExtra("Address");
        TextView titleName = (TextView) findViewById(R.id.name);
        titleName.setText(name);
        TextView typeName = (TextView) findViewById(R.id.type);
        typeName.setText(type);
        TextView addressTxt = (TextView) findViewById(R.id.address);
        addressTxt.setText(address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_info_activity, menu);
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
}
