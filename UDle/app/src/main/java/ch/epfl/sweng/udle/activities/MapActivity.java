package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;
import ch.epfl.sweng.udle.network.DataManager;

public class MapActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location location = new Location("");
    private String deliveryAddress = "";
    private AutoCompleteTextView autoCompView;
    private AlertDialog.Builder dlgAlert;
    private Marker selected_position;
    private boolean displayGpsMessage = true;
    private DataManager data;
    private boolean dlgAlertcountCreated = false;
    private String nonNullLocationProvider = "nonNullLocationProvider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        dlgAlert = new AlertDialog.Builder(this);
        data = new DataManager();
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);
        CheckEnableGPS();
        setUpMapIfNeeded();
        hideKeyborad();
    }

    public void setDeliveryAdress(String addr){
        deliveryAddress = addr;
    }
    public String getDeliveryAdress(){
        return deliveryAddress;
    }

    private void hideKeyborad() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
    @Override
    protected void onResume(){
        super.onResume();
        CheckEnableGPS();
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        LatLng latLng = GooglePlacesAutocompleteAdapter.getLatLngFromId(((GooglePlacesAutocompleteAdapter) adapterView.getAdapter()).getItem_Id(position));
        setDeliveryAddressLocation(latLng, str);
        setCamera(latLng);
    }

    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToMenuActivity(View view) {
        if(isLocationInitialised() && !getDeliveryAdress().equals("")) {
            OrderElement orderElement = new OrderElement();
            orderElement.setDeliveryLocation(getLocation());
            orderElement.setDeliveryAddress(getDeliveryAdress());
            Orders.setActiveOrder(orderElement);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, R.string.incorrectLocation, Toast.LENGTH_SHORT).show();
        }
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MenuMap_GoogleMaps))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setDeliveryAddressLocation(LatLng latLng, String str) {
        Location tempLocation = new Location("");
        tempLocation.setLatitude(latLng.latitude);
        tempLocation.setLongitude(latLng.longitude);
        setLocation(tempLocation);

        if (selected_position == null)
            selected_position = this.mMap.addMarker(new MarkerOptions().position(latLng).title(str));
        else {
            selected_position.setPosition(latLng);
            selected_position.setTitle(str);
        }

        setDeliveryAdress(str);
        data.setUserLocation(location.getLatitude(), location.getLongitude());
        runOnUiThread(new Runnable() {
            public void run() {
                if (!getDeliveryAdress().equals(""))
                    autoCompView.setText(deliveryAddress);
            }
        });
    }
    private void setCamera(LatLng latLng) {
        // Show the argument location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private String getCompleteAddressString(double latitude, double longitude) {
        String Address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder sb = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                Address = sb.toString();
                if (Address.endsWith(", ")) {
                    Address = Address.substring(0, Address.length() - 2);
                }
            } else {
                Log.w("Current location", "No address returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Address;
    }


    private void CheckEnableGPS(){
        if (displayGpsMessage){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if(dlgAlertcountCreated) {
                    dlgAlert.create().cancel();
                    dlgAlertcountCreated = false;
                }
                Toast.makeText(this, R.string.locationEnable, Toast.LENGTH_SHORT).show();
            }else{
                if (!dlgAlertcountCreated) {
                    dlgAlertcountCreated = true;
                    dlgAlert.setMessage(R.string.mapActivityNoGps);
                            dlgAlert.setTitle(R.string.app_name);
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dlgAlert.create().cancel();
                                    dlgAlertcountCreated = false;
                                    Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
                    dlgAlert.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dlgAlert.create().cancel();
                                    dlgAlertcountCreated = false;
                                    displayGpsMessage = false;
                                }
                            });
                    dlgAlert.setCancelable(false);
                    dlgAlert.create().show();
                }
            }
        }
    }

    private void setLocation(Location loc){
        if (loc != null) {
            this.location = loc;
            this.location.setProvider(nonNullLocationProvider);
        }
    }
    private Location getLocation(){
        return location;
    }
    private boolean isLocationInitialised(){
        if (getLocation().getProvider().equals(nonNullLocationProvider))
            return true;
        return false;
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (!isLocationInitialised()){
                LatLng LatLng = new LatLng(location.getLatitude(), location.getLongitude());
                setDeliveryAddressLocation(LatLng, getCompleteAddressString(location.getLatitude(), location.getLongitude()));
                setCamera(LatLng);
            }
        }
    };

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        setLocation(locationManager.getLastKnownLocation(provider));
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (!isLocationInitialised()){
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }else{
            LatLng LatLng = new LatLng(location.getLatitude(), location.getLongitude());
            setDeliveryAddressLocation(LatLng, getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            setCamera(LatLng);
        }
    }
}

