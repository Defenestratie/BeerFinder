package com.beerfinder.beerfinder;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class BoughtBeer extends Activity {
    ListView beersAtLocationListView;
    ArrayList<String> boughtBeerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_beer);

        boughtBeerList = new ArrayList<>();
        ArrayAdapter boughtBeerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, boughtBeerList);

        beersAtLocationListView = (ListView)findViewById(R.id.beersAtLocationList);
        beersAtLocationListView.setAdapter(boughtBeerAdapter);
    }

    public void addBeer(View v){
        Button button = (Button)v;

        //TODO add to database
    }
}
