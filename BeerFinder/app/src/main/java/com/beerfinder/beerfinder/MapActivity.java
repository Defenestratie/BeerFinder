package com.beerfinder.beerfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static double myLatitude = 0;
    public static double myLongitude = 0;
    private static ArrayList<com.beerfinder.beerfinder.Location> LocationsList = new ArrayList();
    CustomList nameImgAdapter;

    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    String[] staticNameList;
    Bitmap[] staticImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
        Log.i("Tag", "Map opgezet." + myLatitude + "," + myLongitude);

        if (LocationsList.isEmpty()) {
            Log.i("Tag", "Arraylist leeg.");

        } else {
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

    }

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
            for (com.beerfinder.beerfinder.Location location : LocationsList) {
                database.insertLocationIntoDatabase(location);
            }
            database.closeDatabase();
    }

    public static void getLocationList() {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        LocationsList = JsonToDatabase.readJsonInfo();
    }

    private void setMarkers() {
        for (com.beerfinder.beerfinder.Location location : LocationsList) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLon()))
                    .title(location.getName()).icon(location.getTypeIcon(this)));
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), UserPreferences.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void startInfoPage(int position)
    {
        Intent intent = new Intent(getApplicationContext(), LocationInfo_activity.class);
        com.beerfinder.beerfinder.Location info = LocationsList.get(position);
        intent.putExtra("Name", info.getName());
        intent.putExtra("Type",info.getType());
        intent.putExtra("Address", info.getAddress());
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String id = marker.getId();

        id = id.replaceFirst("m", "");
        int position = Integer.parseInt(id);
        Log.d("loc forloop", " id = " + id);

        startInfoPage(position);

        //handle click here
        return false;
    }

    private void setUpMap() {
        mMap.setOnMarkerClickListener(this);

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        if (myLocation == null || (myLatitude != 0 && myLongitude != 0)){
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
        Location myLocation = locationManager.getLastKnownLocation(provider);

        Log.d("Location provider", "" + locationManager.getAllProviders().size());

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        myLatitude = myLocation.getLatitude();

        // Get longitude of the current location
        myLongitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(myLatitude, myLongitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here!"));
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

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    final int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(500).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                //LocationsList.remove(item);
//                                nameImgAdapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//
//                                startInfoPage(position);
//
//
//
//                            }
//                        });
//            }
//
//        });

//        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_single, LocationsList);
        listview.setAdapter(nameImgAdapter);
    }


}



