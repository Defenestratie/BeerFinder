package com.beerfinder.beerfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class BoughtBeer extends Activity {
    ListView beersAtLocationListView;
    ArrayList<String> boughtBeerList;
    Database database = new Database();
    String place_ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_beer);

        Intent intent = getIntent();
        place_ID = intent.getStringExtra("locationID");

        boughtBeerList = new ArrayList<>();
        ArrayAdapter boughtBeerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, boughtBeerList);

        beersAtLocationListView = (ListView)findViewById(R.id.beersAtLocationList);
        beersAtLocationListView.setAdapter(boughtBeerAdapter);

        setSpinner();
        setListView();
    }

    private void setListView() {
        ArrayList<String> list = database.getAllBeerTypesForLocation(place_ID);
        ListView listView = (ListView) findViewById(R.id.beersAtLocationList);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    public void addBeer(View v){
        EditText beerBrand =  (EditText) findViewById(R.id.beerBrandEditText);
        EditText beerName = (EditText) findViewById(R.id.addBeerNameEditText);
        String brand = beerBrand.getText().toString();
        String name = beerName.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerBeerTypes);
        BeerType beertype = (BeerType)spinner.getSelectedItem();
        int id = beertype.getID();

        database.insertBeerIntoDatabase(brand, name, id);
        database.insertIntoBeerLocations(place_ID, id);
    }

    private void setSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.spinnerBeerTypes);
        ArrayAdapter<BeerType> spinnerAdapter = new ArrayAdapter<BeerType>(this,
                android.R.layout.simple_spinner_item, database.getBeerTypes());

        spinner.setAdapter(spinnerAdapter);

    }
}
