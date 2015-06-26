package com.beerfinder.beerfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
        beersAtLocationListView.invalidateViews();
    }

    private boolean checkEditTextBox(){
        EditText beerBrand =  (EditText) findViewById(R.id.beerBrandEditText);
        EditText beerName = (EditText) findViewById(R.id.addBeerNameEditText);
        if(beerBrand.getText().toString().trim().length() == 0
                || beerName.getText().toString().trim().length() == 0){
            return false;
        }else if(beerBrand.getText().toString().trim().length() == 0
                && beerName.getText().toString().trim().length() == 0){
            return false;
        }else{
            return true;
        }

    }

    public void addBeer(View v){
        if(checkEditTextBox()) {
            EditText beerBrand = (EditText) findViewById(R.id.beerBrandEditText);
            EditText beerName = (EditText) findViewById(R.id.addBeerNameEditText);
            String brand = beerBrand.getText().toString();
            String name = beerName.getText().toString();
            Spinner spinner = (Spinner) findViewById(R.id.spinnerBeerTypes);
            BeerType beertype = (BeerType) spinner.getSelectedItem();
            int id = beertype.getID();
            int ID_bier = database.insertBeerIntoDatabase(brand, name, id);
            database.insertIntoBeerLocations(place_ID, ID_bier);
            Log.d("Tag", "Id bier: " + ID_bier);
            setListView();
        }else{
            displayError();
        }
    }

    private void displayError() {
        new AlertDialog.Builder(BoughtBeer.this)
                .setTitle("Error")
                .setMessage("Niet alle velden zijn ingevoerd.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.spinnerBeerTypes);
        ArrayAdapter<BeerType> spinnerAdapter = new ArrayAdapter<BeerType>(this,
                android.R.layout.simple_spinner_item, database.getBeerTypes());

        spinner.setAdapter(spinnerAdapter);

    }
}
