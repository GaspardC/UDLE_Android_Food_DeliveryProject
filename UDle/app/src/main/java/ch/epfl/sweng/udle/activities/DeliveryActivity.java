package ch.epfl.sweng.udle.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

public class DeliveryActivity extends SlideMenuActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        setUpMapIfNeeded();
        slideMenuItems.add(new NavItem("Special Delivery", "option added from the child activity", R.mipmap.ic_launcher));
    }

    public void callDeliveryGuy(View view){
        try {
            Intent my_callIntent = new Intent(Intent.ACTION_CALL);
//            my_callIntent.setData(Uri.parse("tel:" + DataManager.getDeliveryGuyNumber()));
            startActivity(my_callIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e){
            Toast.makeText(getApplicationContext(), "UDle doesn't have the right to pass phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Delivery_GoogleMaps))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Location myLocation;
        try {
            // Get Current Location
            myLocation = locationManager.getLastKnownLocation(provider);

            // Get latitude/ longitude of the current location
            if(myLocation == null)  return;
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + .001, longitude + .001)).title("Restaurant Location").snippet("ETA 5min"));
            LatLng myCoordinates = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 15);
            mMap.animateCamera(yourLocation);
        }catch(SecurityException e){
            Toast.makeText(getApplicationContext(), "You need to enable localisation" + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }


}
