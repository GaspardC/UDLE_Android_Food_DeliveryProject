package ch.epfl.sweng.udle.activities;

import android.content.ActivityNotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

public class DeliveryActivity extends SlideMenuActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        setUpMapIfNeeded();
        placeMarkers();
    }

    public void callDeliveryGuy(View view){
        try {
            /*
            Intent my_callIntent = new Intent(Intent.ACTION_CALL);
//            my_callIntent.setData(Uri.parse("tel:" + DataManager.getDeliveryGuyNumber()));
            startActivity(my_callIntent);
            */
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.errorInYourPhoneCall) + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e){
            Toast.makeText(getApplicationContext(), getString(R.string.rightToPassPhoneCall) + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void setCamera(LatLng latLng) {
        // Show the argument location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    private void placeMarkers() {
        Location deliveryLocation;
        String deliveryAddress;
        // Get delivery location
        deliveryLocation = Orders.getActiveOrder().getDeliveryLocation();
        deliveryAddress = Orders.getActiveOrder().getDeliveryAddress();

        /*
        Location restaurantLocation;
        String restaurantAddress;

        // Get delivery location
        restaurantLocation = Something to do;
        restaurantAddress = Something to do;

        mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(deliveryLocation.getLatitude(), deliveryLocation.getLongitude()))
                        .title("Restaurant address")
                        .snippet(restaurantAddress)
        );
        */
        LatLng latLng = new LatLng(deliveryLocation.getLatitude(), deliveryLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.markerTitle))
                        .snippet(deliveryAddress)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        );

        setCamera(latLng);
    }
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
