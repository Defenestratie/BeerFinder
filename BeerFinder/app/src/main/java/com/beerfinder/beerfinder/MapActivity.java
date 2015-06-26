package com.beerfinder.beerfinder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private GoogleMap mMap = null; // Might be null if Google Play services APK is not available.
    public static double myLatitude = 0.0;
    public static double myLongitude = 0.0;

    //direct locations from JSON
    private static ArrayList<com.beerfinder.beerfinder.Location> LocationsList1 = new ArrayList();

    //JSON locations filtered for beer types
    private static ArrayList<com.beerfinder.beerfinder.Location> LocationsList2 = new ArrayList();

    //List that is drawn on the map
    private static ArrayList<com.beerfinder.beerfinder.Location> LocationsList = new ArrayList();

    CustomList nameImgAdapter = null;

    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    String[] staticNameList = null;
    Bitmap[] staticImageList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);

        Log.i("Tag", "Map opgezet." + myLatitude + "," + myLongitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

    }



    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

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

    };

    public static void setJsonObject() {
        try {
            new JsonToDatabase().execute(Double.toString(myLatitude), Double.toString(myLongitude)).get();
        } catch (ExecutionException ex) {
            Log.i("Tag", "JsonObject ophalen onderbroken!");

        } catch (InterruptedException ex) {
            Log.i("Tag", "JsonObject ophalen onderbroken!");
        }

    }

    private void InsertToDatabase() {
        Database database = new Database();
        for (com.beerfinder.beerfinder.Location location : LocationsList1) {
            database.insertLocationIntoDatabase(location);
        }
    }

    public static void getLocationList1() {
        LocationsList1 = JsonToDatabase.readJsonInfo();
    }

    private void setMarkers() {

        for (com.beerfinder.beerfinder.Location location : LocationsList) {

            if(location.getOpen_now().equals("Open") || location.getOpen_now().equals("Onbekend")) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLat(), location.getLon()))
                        .title(location.getName()).icon(location.getTypeIcon(this)));
            }
        }
    }

    public static void checkForBeerPreferences(){
        if(!LocationsList1.isEmpty()) {
            if (!UserPreferences.getBeerTypes().isEmpty() || !UserPreferences.getBeerBrands().isEmpty()) {
                LocationsList2 = Database.filterByBeer(LocationsList1);
                LocationsList = LocationsList2;
            } else {
                LocationsList = LocationsList1;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Database database = new Database();
        database.closeDatabase();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        checkForBeerPreferences();

        if (LocationsList.isEmpty()) {
            Log.i("Tag", "Arraylist leeg.");
            setLocation();
            setJsonObject();
            getLocationList1();
            checkForBeerPreferences();
        }

        setMarkers();
        Log.i("Tag", "Markers geplaatst");

        new Thread(new Runnable() {
            public void run() {
                setListview();
                Log.i("Tag", "Listview gemaakt.");
            }
        }).start();

        InsertToDatabase();
    }

    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position).toString();
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void openUserPreferences(View v){
        startActivity(new Intent(getApplicationContext(), UserPreferences.class));
    }

    private void startInfoPage(int position) {
        Intent intent = new Intent(getApplicationContext(), LocationInfo_activity.class);
        com.beerfinder.beerfinder.Location info = LocationsList.get(position);
        intent.putExtra("Name", info.getName());
        intent.putExtra("Type", info.getType());
        intent.putExtra("Address", info.getAddress());
        intent.putExtra("Open", info.getOpen_now());
        intent.putExtra("ID", info.getID());
        intent.putExtra("Open", info.getOpen_now());
        Log.i("Tag", "info.getID()" + info.getID());
        startActivity(intent);
    }

    Marker lastMarker = null;


    @Override
    public boolean onMarkerClick(final Marker marker) {


        marker.showInfoWindow();
        Log.d("onMarkerClick", "Started!");

        try {
            Log.d("onMarkerClick", "marker.getId = " + marker.getId() + " lastMarker.getId = " + lastMarker.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String id = marker.getId();

        id = id.replaceFirst("m", "");
        int position = Integer.parseInt(id);
        Log.d("loc forloop", " id = " + id);

        if (lastMarker != null && marker.getId().equals(lastMarker.getId())) {
            startInfoPage(position);
        }

        lastMarker = marker;


        //handle click here
        return false;
    }

    private void setUpMap() {
        mMap.setOnMarkerClickListener(this);

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        if (myLocation == null || (myLatitude != 0 && myLongitude != 0)) {
            Log.d("Location provider", "No  location detected!");
            try {
                Thread.sleep(500);
                setLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            setLocation();
        }
    }

    public void setLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Log.d("get current location", "start");
        final Location[] myLocation = {locationManager.getLastKnownLocation(provider)};
        myLocation[0] = null;
        Log.d("get current location", "getLastKnowLocation try " + myLocation[0]);
        if (myLocation[0] == null) {
            Log.d("get current location", "inside if loop");
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    Log.d("got location", "Got location" + location);
                    myLocation[0] = location;
                }
            };

            Log.d("get current location", "after if loop " + myLocation[0]);

            MyLocation myLocation2 = new MyLocation();
            myLocation2.getLocation(this, locationResult);
        }

        Log.d("Location provider", "" + locationManager.getAllProviders().size());

        while(myLocation[0] == null && (myLatitude == 0.0 && myLongitude == 0.0))
        {
            try {
                Thread.sleep(100);
                Log.d("sleep", "sleeping 100 miliseconds");
                Toast.makeText(getApplicationContext(),"Waiting for location!" ,Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(myLatitude == 0.0 && myLongitude == 0.0) {
            // Get latitude of the current location
            myLatitude = myLocation[0].getLatitude();

            // Get longitude of the current location
            myLongitude = myLocation[0].getLongitude();
        }

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(myLatitude, myLongitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        //mMap.addMarker(new MarkerOptions().position(latLng).title("You are here!"));
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private void setListview() {
        final ListView listview = (ListView) findViewById(R.id.listViewPlaces);

        for (com.beerfinder.beerfinder.Location location : LocationsList) {
            nameList.add(location.getName());
            imageList.add(location.getListTypeIcon(this));
        }

        staticNameList = new String[nameList.size()];
        staticNameList = nameList.toArray(staticNameList);

        staticImageList = new Bitmap[imageList.size()];
        staticImageList = imageList.toArray(staticImageList);

        nameImgAdapter = new CustomList(this, staticNameList, staticImageList);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, final long id) {

                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(500).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //LocationsList1.remove(item);
                                nameImgAdapter.notifyDataSetChanged();
                                view.setAlpha(1);

                                startInfoPage(position);
                            }
                        });
            }

        });


        listview.setAdapter(nameImgAdapter);
    }
}



