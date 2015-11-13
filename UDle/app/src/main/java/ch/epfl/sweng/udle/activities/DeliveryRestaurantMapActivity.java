package ch.epfl.sweng.udle.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

public class DeliveryRestaurantMapActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean showMap = true;
    private ListView listView;
    final ArrayList<OrderElement> waitingOrders = getWaitingOrders(new ArrayList<OrderElement>());
    final ArrayList<OrderElement> currentOrders = Orders.getCurrentOrders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_restaurant_map);
        setUpMapIfNeeded();
        showWaitingOrders();
        setUpListView();

    }



    private void setUpListView() {
        listView = (ListView) findViewById(R.id.listOrderRestaurantMap);
        if(showMap){
            listView.setVisibility(View.GONE);
        }
        else{
            listView.setVisibility(View.VISIBLE);
            populateListView();
        }
    }

    private void populateListView() {

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        ArrayList<String> ordersAdress = new ArrayList<>();

        for(OrderElement order : waitingOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Waiting Order").snippet(deliveryAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            ordersAdress.add(deliveryAddress);
        }
        for(OrderElement order : currentOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(latLng).title("Confirmed Order").snippet(deliveryAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            ordersAdress.add(deliveryAddress);
        }


        for(int i=0;i<ordersAdress.size();i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("address", "address : " + ordersAdress.get(i));
//            hm.put("cur","Currency : " + currency[i]);
//            hm.put("flag", Integer.toString(flags[i]) );
            hm.put("image", Integer.toString(R.drawable.logoburger) );
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "image", "address" };

        // Ids of views in listview_layout
//        int[] to = { R.id.addressDelivery};
        int[] to = { R.id.iconListDelivery,R.id.addressDelivery};


        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(this, aList, R.layout.list_item_restaurant_delivery, from, to);


        listView.setAdapter(adapter);
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.DeliveryMap_GoogleMaps))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude/ longitude of the current location
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map

        LatLng myCoordinates = new LatLng(latitude, longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 12);
        mMap.animateCamera(yourLocation);
                return;
            }
        }
    }

    private void showWaitingOrders(){



        for(OrderElement order : waitingOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Waiting Order").snippet(deliveryAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        for(OrderElement order : currentOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Confirmed Order").snippet(deliveryAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (OrderElement order : waitingOrders) {
                    if (order.getDeliveryAddress().equals(marker.getSnippet())) { //TODO: Instead of compare with the address, compare with the id of the command for example.
                        goToDeliveryCommandDetail(order);
                    }
                }
                for (OrderElement order : currentOrders) {
                    if (order.getDeliveryAddress().equals(marker.getSnippet())) { //TODO: Instead of compare with the address, compare with the id of the command for example.
                        goToDeliveryCommandDetail(order);
                    }
                }
            }
        });
    }



    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToDeliveryCommandDetail(OrderElement order) {
        Orders.setActiveOrder(order);
        Intent intent = new Intent(this, DeliverCommandDetailActivity.class);
        Orders.setActiveOrder(order);
        startActivity(intent);
    }



    /** JUST FOR TEST**/
    public ArrayList<OrderElement> getWaitingOrders(ArrayList<OrderElement> orders){
        //return DataManager.getPendingOrdersForARestaurantOwner(); TODO: When DataManager is uptaded to the master

        //BASIC DATA FOR TESTS
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.KETCHUP);
        menu1.addToOptions(OptionsTypes.SALAD);
        OrderElement orderElement1 = new OrderElement();
        orderElement1.addMenu(menu1);
        orderElement1.addToDrinks(DrinkTypes.BEER);
        Location location1 = new Location("");
        location1.setLatitude(46.519);
        location1.setLongitude(6.566);
        orderElement1.setDeliveryLocation(location1);
        orderElement1.setDeliveryAddress("Address for the deliver 1, 1002 SwEng");
        orders.add(orderElement1);

        Menu menu2 = new Menu();
        menu2.setFood(FoodTypes.BURGER);
        menu2.addToOptions(OptionsTypes.OIGNON);
        menu2.addToOptions(OptionsTypes.TOMATO);
        OrderElement orderElement2 = new OrderElement();
        orderElement2.addMenu(menu2);
        orderElement2.addToDrinks(DrinkTypes.COCA);
        orderElement2.addToDrinks(DrinkTypes.WATER);
        Location location2 = new Location("");
        location2.setLatitude(46.539);
        location2.setLongitude(6.556);
        orderElement2.setDeliveryLocation(location2);
        orderElement2.setDeliveryAddress("Address for the deliver 2, 1002 SwEng");
        orders.add(orderElement2);

        Menu menu3 = new Menu();
        menu3.setFood(FoodTypes.BURGER);
        menu3.addToOptions(OptionsTypes.OIGNON);
        menu3.addToOptions(OptionsTypes.TOMATO);
        Menu menu3b = new Menu();
        menu3b.setFood(FoodTypes.KEBAB);
        menu3b.addToOptions(OptionsTypes.OIGNON);
        menu3b.addToOptions(OptionsTypes.TOMATO);
        menu3b.addToOptions(OptionsTypes.ALGERIENNE);
        OrderElement orderElement3 = new OrderElement();
        orderElement3.addMenu(menu3);
        orderElement3.addMenu(menu3b);
        orderElement3.addToDrinks(DrinkTypes.BEER);
        orderElement3.addToDrinks(DrinkTypes.WATER);
        Location location3 = new Location("");
        location3.setLatitude(46.639);
        location3.setLongitude(6.496);
        orderElement3.setDeliveryLocation(location3);
        orderElement3.setDeliveryAddress("Address for the deliver 3, 1002 SwEng");
        orders.add(orderElement3);

        return orders;
    }

    //When button clicked
    public void switchOrderList(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment mapFrag = getSupportFragmentManager().findFragmentById(R.id.DeliveryMap_GoogleMaps);
        Button buttonSwitch = (Button) findViewById(R.id.button_list_mode);
        if(showMap) {
            ft.hide(mapFrag).commit();
            showMap = !showMap;
            setUpListView();
            buttonSwitch.setText("Switch to Map Mode");
        }
        else{
            ft.show(mapFrag).commit();
            showMap = !showMap;
            setUpListView();
            buttonSwitch.setText("Switch to List Mode");

        }
    }
}

