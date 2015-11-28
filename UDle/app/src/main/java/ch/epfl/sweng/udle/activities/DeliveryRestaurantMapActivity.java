package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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


/**
 * Activity reserved for restaurant user only
 *
 * In this activity the restaurant can visualize the orders around him.
 * He can both see the current orders (assigned to him) or the waiting orders (not yet validated by him)
 *
 *The restaurant can visualize theses orders as pins on the map or as elements in a list
 *
 * He can interact with these orders by clicking on them
 */

public class DeliveryRestaurantMapActivity extends SlideMenuActivity {


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean showMap = true;
    private ListView listView;
    private ArrayList<OrderElement> waitingOrders = new ArrayList<>();
    private ArrayList<OrderElement> currentOrders = new ArrayList<>();

    final Handler h = new Handler();
    final int delay = 30000; //30 seconds in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_restaurant_map);

        setUpMapIfNeeded();


        h.postDelayed(new Runnable() {
            public void run() {
                Log.i("AAAAAAAAAAAAAaa", "=======================JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ=================================");

                if (changeInWaitingOrders() || changeInCurrentOrders()){
                    Log.i("AAAAAAAAAAAAAaa", "=======================AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=================================");
                    showWaitingOrders();
                    setUpListView();
                }

                h.postDelayed(this, delay);
            }
        }, 0);


    }

    /*
    Check if there was a change in the waiting orders for the restaurant.
    If change => refresh the display
    If no change => Do nothing
     */

    private boolean changeInWaitingOrders(){
        //Retrieve list from server
        ArrayList<OrderElement> waitingOrdersFromServe = DataManager.getPendingOrdersForARestaurantOwner();

        if (waitingOrdersFromServe.size() == 0){
            if (waitingOrders.size() != 0){
                waitingOrders = waitingOrdersFromServe;
                return true;
            }
            else{
                return false;
            }
        }

        //In order to check if there was a change or not, we compare the objectID of the list retrieve from server and the one already on device.
        //So use this int to see if the retrieved list is the same or not has the local one.
        int checkSameList = 0;

        //Putting all objectIds of local waitingOrders list into a new list
        ArrayList<String> currentWaitingOrdersObjectID = new ArrayList<>();
        for (OrderElement orderElement: waitingOrders){
            currentWaitingOrdersObjectID.add(orderElement.getObjectId());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : waitingOrdersFromServe){
            if (currentWaitingOrdersObjectID.contains(orderElement.getObjectId())){
                checkSameList ++;
            }
        }

        if (waitingOrdersFromServe.size() == checkSameList){
            //Lists are not the same. Need to refresh
            waitingOrders = waitingOrdersFromServe;
            return true;
        }
        else {
            //Server list is the same as the displayed one. Do nothing.
            return false;
        }
    }
    private boolean changeInCurrentOrders(){
        //Retrieve list from server
        ArrayList<OrderElement> currentOrdersFromServe = DataManager.getCurrentOrdersForARestaurantOwner();

        if (currentOrdersFromServe.size() == 0){
            currentOrders = currentOrdersFromServe;
            return true;
        }

        //In order to check if there was a change or not, we compare the objectID of the list retrieve from server and the one already on device.
        //So use this int to see if the retrieved list is the same or not has the local one.
        int checkSameList = 0;

        //Putting all objectIds of local waitingOrders list into a new list
        ArrayList<String> currentCurrentOrdersObjectID = new ArrayList<>();
        for (OrderElement orderElement: currentOrders){
            currentCurrentOrdersObjectID.add(orderElement.getObjectId());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : currentOrdersFromServe){
            if (currentCurrentOrdersObjectID.contains(orderElement.getObjectId())){
                checkSameList ++;
            }
        }

        if (currentOrdersFromServe.size() == checkSameList){
            //Lists are not the same. Need to refresh
            currentOrders = currentOrdersFromServe;
            return true;
        }
        else {
            //Server list is the same as the displayed one. Do nothing.
            return false;
        }
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


    /**
     * method used to fill (and display) the current and waiting orders
     */
    private void populateListView() {

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        ArrayList<String> ordersAdress = new ArrayList<>();
        int i = 1;
        for(OrderElement order : waitingOrders) {

            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            ordersAdress.add(deliveryAddress);
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("numCommande", "#" + i+" ");
            hm.put("address", ordersAdress.get(i - 1));
            hm.put("image", Integer.toString(R.drawable.logoburger));
            aList.add(hm);
            i++;
        }
        for(OrderElement order : currentOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            ordersAdress.add(deliveryAddress);
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("numCommande", "#" + i+" ");
            hm.put("address", ordersAdress.get(i-1));
            hm.put("image", Integer.toString(R.drawable.logogreen) );
            aList.add(hm);
            i++;
        }

        // Keys used in Hashmap
        String[] from = { "image","numCommande", "address" };

        //Link the adapter to the xml items
        int[] to = { R.id.iconListDelivery,R.id.numCommandeDeliveryRestaurant,R.id.addressDelivery};

        // Instantiating an adapter to store each items
        // R.layout.listView_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(this, aList, R.layout.list_item_restaurant_delivery, from, to);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Open the browser here
                HashMap<String, String> hm = (HashMap<String, String>) parent.getItemAtPosition(position);
                String adress = hm.get("address");

                for (OrderElement order : waitingOrders) {
                    if (order.getDeliveryAddress().equals(adress)) { //TODO: Instead of compare with the address, compare with the id of the command for example.
                        goToDeliveryCommandDetail(order);
                    }
                }
                for (OrderElement order : currentOrders) {
                    if (order.getDeliveryAddress().equals(adress)) { //TODO: Instead of compare with the address, compare with the id of the command for example.
                        goToDeliveryCommandDetail(order);
                    }
                }
            }

        });
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

    private void setCamera(LatLng latLng) {
        // Show the argument location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void showWaitingOrders(){
        boolean initialised = false;

        mMap.clear();

        for(OrderElement order : waitingOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (!initialised) {
                setCamera(latLng); // should be set on the restaurant location
                initialised = true;
            }
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(getResources().getString(R.string.WaitingOrders))
                    .snippet(deliveryAddress)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        for(OrderElement order : currentOrders) {
            Location location = order.getDeliveryLocation();
            String deliveryAddress = order.getDeliveryAddress();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(getResources().getString(R.string.ConfirmedOrders))
                    .snippet(deliveryAddress)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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


    /** Called when the user clicks the switch_mode button
     *  It either display orders in a list or in the fragment
     * */
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

    /** Disable back button here
     * Prevent the restaurant to return to an offer already validated */
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        h.removeCallbacksAndMessages(null);
        super.onPause();
    }
}

