package ch.epfl.sweng.udle.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;


public class MapActivity extends SlideMenuActivity implements AdapterView.OnItemClickListener, GoogleMap.OnMarkerClickListener {

    private ArrayList<Marker> listeMarkers;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location location = new Location("");
    private LinearLayout markerLayout;
    private String deliveryAddress = "";
    private AutoCompleteTextView autoCompView;
    private GooglePlacesAutocompleteAdapter googleAdapter;
    private AlertDialog.Builder dlgAlert;
    private boolean displayGpsMessage = true;
    private boolean dlgAlertcountCreated = false;
    private String nonNullLocationProvider = "nonNullLocationProvider";
    private OrderElement orderElement = new OrderElement();
    final Handler handler = new Handler(); //The list of waiting and current Orders is refresh each 'delay' milliseconds.
    final int delay = 30000; //30 seconds in milliseconds
    private boolean markerHidden = true;
    private boolean afterFirstChange = true;
    private ArrayList<OrderElement> waitingOrders = new ArrayList<>(); //Orders in the restaurant range that have no restaurant assigned to. Status of order: Waiting
    private ArrayList<OrderElement> currentOrders = new ArrayList<>(); //Orders that the restaurant already accept to deliverd. Status of order: EnRoute
    private HashMap<Integer, OrderElement> objectIdHashMapForList; //HashMap between the index of order shown in the list view and the specific order.
    private HashMap<String, OrderElement> objectIdHashMapForMap; //HashMap between the index of order shown in the map and the specific order.

    private int timeBetweenAddrRequest = 300;
    private boolean firstTimeCalled = true;
    final Handler handler2 = new Handler();
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            if (isLocationInitialised() || !displayGpsMessage) {
                googleAdapter.setEnableAutocomplete(false);
                LatLng LatLng = mMap.getCameraPosition().target;
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                setDeliveryAddressLocation(LatLng, getCompleteAddressString(LatLng.latitude, LatLng.longitude), true);
            }
            if (markerHidden && afterFirstChange) {
                markerLayout.setVisibility(LinearLayout.VISIBLE);
                markerHidden = false;
                afterFirstChange = false;
            } else {
                afterFirstChange = true;
            }
        }
    };
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


/*        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("movie", "The Matrix");
        ParseCloud.callFunctionInBackground("hello", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                Log.d("cloudCode",object.toString());
            }
        });*/
        listeMarkers = new ArrayList<>();
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        markerHidden = false;
        dlgAlert = new AlertDialog.Builder(this);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        googleAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.list_item);
        autoCompView.setAdapter(googleAdapter);
        googleAdapter.setNotifyOnChange(true);
        autoCompView.setOnItemClickListener(this);
        CheckEnableGPS();
        setUpMapIfNeeded();
        hideKeyborad();

        /*mMap.setOnMarkerClickListener(this);
        placeMarkers();
        hideKeyborad();
        handler.postDelayed(getMapRunnable(), 0);*/



    }




    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToDeliveryCommandDetail(OrderElement order, boolean isCurrent) {
        Orders.setActiveOrder(order);


        Intent intent = new Intent(this, RecapActivity.class);
        intent.putExtra("from", "Map");
        startActivity(intent);


    }




    protected void showCustomDialog() {

        dialog = new Dialog(MapActivity.this,
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);

        /*etSearch = (EditText) dialog.findViewById(R.id.etsearch);
        btnSearch = (Button) dialog.findViewById(R.id.btnsearch);
        btnCancel = (Button) dialog.findViewById(R.id.btncancel);

        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);*/

        dialog.show();
    }

    /**
     * @return Runnable: Each 'delay' milliseconds, call the function run function. This run function check if orders need to be refresh and do it if needed.
     */
    private Runnable getMapRunnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean waitingOrdersChange = changeInWaitingOrders();
                boolean currentOrdersChange = changeInCurrentOrders();

                if (waitingOrdersChange || currentOrdersChange) {
                    showOrdersOnMap();
                }
                handler.postDelayed(this, delay);
            }
        };
        return runnable;
    }


    /**
     * Display the waiting and the current orders on the Google Map.
     * Display each order via a marker. Red color for waiting orders, Green for current ones.
     */
    private void showOrdersOnMap() {
        mMap.clear();
        objectIdHashMapForMap = new HashMap<>();
        int objectIdHashMapIndex = 1;

        Location rlocation = DataManager.getUserLocation();
        LatLng rLatLng = new LatLng(rlocation.getLatitude(), rlocation.getLongitude());
        setCamera(rLatLng);


        for (OrderElement order : waitingOrders) {
            Location location = order.getDeliveryLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            String deliveryAddress = order.getDeliveryAddress();

            String markerTitle = getResources().getString(R.string.WaitingOrders) + " #" + objectIdHashMapIndex;
            objectIdHashMapForMap.put(markerTitle, order);
            objectIdHashMapIndex++;

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(markerTitle)
                    .snippet(deliveryAddress)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        for (OrderElement order : currentOrders) {
            Location location = order.getDeliveryLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            String deliveryAddress = order.getDeliveryAddress();

            String markerTitle = getResources().getString(R.string.ConfirmOrders) + " #" + objectIdHashMapIndex;
            objectIdHashMapForMap.put(markerTitle, order);
            objectIdHashMapIndex++;

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(markerTitle)
                    .snippet(deliveryAddress)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String markerTitle = marker.getTitle();
                OrderElement order = objectIdHashMapForMap.get(markerTitle);

                boolean isCurrent = false;
                if (markerTitle.contains("Current")) {
                    isCurrent = true;
                }

                goToDeliveryCommandDetail(order, isCurrent);
            }
        });
    }

    /**
     *
     * Check if there was a change in the waiting orders for the restaurant.
     * If change => refresh the display
     * If no change => Do nothing
     *
     */
    private boolean changeInWaitingOrders() {
        //Retrieve list from server
        ArrayList<OrderElement> waitingOrdersFromServe = DataManager.getWaitingOrdersForAClient();

        if (waitingOrdersFromServe.size() == 0) {
            if (waitingOrders.size() != 0) {
                waitingOrders = waitingOrdersFromServe;
                return true;
            } else {
                return false;
            }
        }

        //In order to check if there was a change or not, we compare the objectID of the list retrieve from server and the one already on device.
        //So use this int to see if the retrieved list is the same or not has the local one.
        int checkSameList = 0;

        //Putting all objectIds of local waitingOrders list into a new list
        ArrayList<String> currentWaitingOrdersObjectID = new ArrayList<>();
        for (OrderElement orderElement : waitingOrders) {
            currentWaitingOrdersObjectID.add(orderElement.getUserOrderInformationsID());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : waitingOrdersFromServe) {
            if (currentWaitingOrdersObjectID.contains(orderElement.getUserOrderInformationsID())) {
                checkSameList++;
            }
        }

        if (waitingOrdersFromServe.size() != checkSameList) {
            //Lists are not the same. Need to refresh
            waitingOrders = waitingOrdersFromServe;
            return true;
        } else {
            //Server list is the same as the displayed one. Do nothing.
            return false;
        }
    }


    /**
     *
     * Check if there was a change in the current orders for the restaurant.
     * If change => refresh the display
     * If no change => Do nothing
     *
     */
    private boolean changeInCurrentOrders() {
        //Retrieve list from server
        ArrayList<OrderElement> currentOrdersFromServe = DataManager.getEnRouteOrdersForAClient();

        if (currentOrdersFromServe.size() == 0) {
            currentOrders = currentOrdersFromServe;
            return true;
        }

        //In order to check if there was a change or not, we compare the objectID of the list retrieve from server and the one already on device.
        //So use this int to see if the retrieved list is the same or not has the local one.
        int checkSameList = 0;

        //Putting all objectIds of local waitingOrders list into a new list
        ArrayList<String> currentCurrentOrdersObjectID = new ArrayList<>();
        for (OrderElement orderElement : currentOrders) {
            currentCurrentOrdersObjectID.add(orderElement.getUserOrderInformationsID());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : currentOrdersFromServe) {
            if (currentCurrentOrdersObjectID.contains(orderElement.getUserOrderInformationsID())) {
                checkSameList++;
            }
        }

        if (currentOrdersFromServe.size() != checkSameList) {
            //Lists are not the same. Need to refresh
            currentOrders = currentOrdersFromServe;
            return true;
        } else {
            //Server list is the same as the displayed one. Do nothing.
            return false;
        }
    }


    public void setDeliveryAdress(String addr) {
        deliveryAddress = addr;
    }

    public String getDeliveryAdress() {
        return deliveryAddress;
    }

    private void hideKeyborad() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataManager.getUser() == null) {
            Intent login = new Intent(this, ProfileActivity.class);
            startActivity(login);
        }
        CheckEnableGPS();
/*
        placeMarkers();
*/
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        LatLng latLng = GooglePlacesAutocompleteAdapter.getLatLngFromId(((GooglePlacesAutocompleteAdapter) adapterView.getAdapter()).getItem_Id(position));
        setDeliveryAddressLocation(latLng, str, false);
        setCamera(latLng);
    }

    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToMenuActivity(View view) {

        if (storeNearbyRestaurants()) {
            if (isLocationInitialised() && !getDeliveryAdress().equals("")) {
                orderElement = new OrderElement();
                orderElement.setDeliveryLocation(getLocation());
                orderElement.setDeliveryAddress(getDeliveryAdress());
                orderElement.setOrderedUserName(DataManager.getUserName());
                Orders.setActiveOrder(orderElement);
                showCustomDialog();

                String name;
                ArrayList<ParseUser> nearbyRestaurants = DataManager.nearbyRestaurants;
                ArrayList<String> urlLogos = new ArrayList<>();
                ArrayList<Number> marksRestos = new ArrayList<>();

                for (final ParseUser resto :nearbyRestaurants){

                    ParseFile pLogo = resto.getParseFile("RestaurantLogo");
                    String urlLogo = pLogo.getUrl();
                    urlLogos.add(urlLogo);
                    marksRestos.add(DataManager.getAverageMarkRestaurant(resto));
                }
                ListView listView = (ListView) dialog.findViewById(R.id.listLogo);
                listView.setAdapter(new ImageListAdapter(MapActivity.this, urlLogos,marksRestos));
                /*Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);*/
            } else {
                Toast.makeText(this, R.string.incorrectLocation, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.noRestaurantAvailable, Toast.LENGTH_SHORT).show();
        }
    }

    public void fasterButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    private void setDeliveryAddressLocation(LatLng latLng, String str, final boolean changeAutocompView) {
        Location tempLocation = new Location("");
        tempLocation.setLatitude(latLng.latitude);
        tempLocation.setLongitude(latLng.longitude);
        setLocation(tempLocation);

        setDeliveryAdress(str);


        runOnUiThread(new Runnable() {
            public void run() {
                if (changeAutocompView) {
                    if (!getDeliveryAdress().equals(""))
                        autoCompView.setText(deliveryAddress);
                    else
                        autoCompView.setText(R.string.invalidAddress);
                }
            }
        });
    }

    private void setCamera(LatLng latLng) {
        // Show the argument location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void placeMarkers() {
        for (Marker marker : listeMarkers) {
            marker.remove();
        }
        listeMarkers.clear();

        Location deliveryLocation;
        String deliveryAddress;

        if (Orders.getActiveOrder() != null) {
            deliveryLocation = Orders.getActiveOrder().getDeliveryLocation();
            deliveryAddress = Orders.getActiveOrder().getDeliveryAddress();
            LatLng latLng = new LatLng(deliveryLocation.getLatitude(), deliveryLocation.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(getResources().getString(R.string.markerTitle))
                            .snippet(deliveryAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
            listeMarkers.add(marker);
        }
        ArrayList<OrderElement> currentOrd = Orders.getCurrentOrders();
        if (currentOrd == null) {
            return;
        }
        if (currentOrd.size() == 0)
            return;
        for (OrderElement orderElem : currentOrd) {
            if (orderElem != null) {
                deliveryLocation = orderElem.getDeliveryLocation();
                deliveryAddress = orderElem.getDeliveryAddress();
                LatLng latLng = new LatLng(deliveryLocation.getLatitude(), deliveryLocation.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(getResources().getString(R.string.markerTitle))
                                .snippet(deliveryAddress)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
                listeMarkers.add(marker);
            }
        }
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


    private void CheckEnableGPS() {
        if (displayGpsMessage) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (dlgAlertcountCreated) {
                    dlgAlert.create().cancel();
                    dlgAlertcountCreated = false;
                }
                Toast.makeText(this, R.string.locationEnable, Toast.LENGTH_SHORT).show();
            } else {
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

    private void setLocation(Location loc) {
        if (loc != null) {
            this.location = loc;
            this.location.setProvider(nonNullLocationProvider);
            DataManager.setUserLocation(loc);
            LatLng LatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            if (firstTimeCalled){
                setCamera(LatLng);
            }
        }
    }

    private Location getLocation() {
        return location;
    }

    private boolean isLocationInitialised() {
        if (getLocation().getProvider().equals(nonNullLocationProvider))
            return true;
        return false;
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (!isLocationInitialised()) {
                LatLng LatLng = new LatLng(location.getLatitude(), location.getLongitude());
                setDeliveryAddressLocation(LatLng, getCompleteAddressString(location.getLatitude(), location.getLongitude()), true);
                DataManager.setUserLocation(location);
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
        Location loc;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            loc = locationManager.getLastKnownLocation(provider);
        } catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            loc = new Location("");
        }

        // Get Current Location
        setLocation(loc);
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (!isLocationInitialised()){
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }else{
            LatLng LatLng = new LatLng(location.getLatitude(), location.getLongitude());
            setDeliveryAddressLocation(LatLng, getCompleteAddressString(location.getLatitude(), location.getLongitude()), true);
            setCamera(LatLng);
        }
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                if (firstTimeCalled) {
                    handler2.postDelayed(r, timeBetweenAddrRequest);
                    firstTimeCalled = false;
                }
                else {
                    handler2.removeCallbacks(r);
                    handler2.postDelayed(r, timeBetweenAddrRequest);
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker mar) {
        if(true) {
            // if marker source is clicked
            markerLayout.setVisibility(LinearLayout.INVISIBLE );
            markerHidden = true;
            afterFirstChange = false;
            return false;
        }
        return false;
    }

    private boolean storeNearbyRestaurants(){
        //Put current location in parse.com
        ParseGeoPoint currentLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        ParseUser currentUser = DataManager.getUser();
        currentUser.put("Location", currentLocation);

        //Find nearby restaurants and store in server
        //NEED TO CHANGE THIS DEPENDING ON HOW WE USE NEARBY RESTAURANTS
        return DataManager.getRestaurantsNearTheUser();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }


}
