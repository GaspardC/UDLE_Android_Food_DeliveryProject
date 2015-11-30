package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;


/**
 * Activity reserved for restaurant user only
 *
 * In this activity the restaurant can visualize the orders around him.
 * He can both see the current orders (assigned to him) or the waiting orders (not yet validated by a restaurant)
 *
 * The restaurant can visualize theses orders as pins on the map or as elements in a list
 *
 * He can interact with these orders by clicking on them
 */

public class DeliveryRestaurantMapActivity extends SlideMenuActivity {


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean showMap = true;
    private ListView listView;
    private ArrayList<OrderElement> waitingOrders = new ArrayList<>(); //Orders in the restaurant range that have no restaurant assigned to. Status of order: Waiting
    private ArrayList<OrderElement> currentOrders = new ArrayList<>(); //Orders that the restaurant already accept to deliverd. Status of order: EnRoute
    private HashMap<Integer, OrderElement> objectIdHashMapForList; //HashMap between the index of order shown in the list view and the specific order.
    private HashMap<String, OrderElement> objectIdHashMapForMap; //HashMap between the index of order shown in the map and the specific order.

    final Handler handler = new Handler(); //The list of waiting and current Orders is refresh each 'delay' milliseconds.
    final int delay = 30000; //30 seconds in milliseconds



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_restaurant_map);

        setUpMapIfNeeded();

        handler.postDelayed(getMapRunnable(), 0);
    }

    /**
     * @return Runnable: Each 'delay' milliseconds, call the function run function. This run function check if orders need to be refresh and do it if needed.
     */
    private Runnable getMapRunnable(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean waitingOrdersChange = changeInWaitingOrders();
                boolean currentOrdersChange = changeInCurrentOrders();

                if (waitingOrdersChange || currentOrdersChange) {
                    showOrdersOnMap();
                    setUpListView();
                }
                handler.postDelayed(this, delay);
            }
        };
        return runnable;
    }

    /**
     *
     * Check if there was a change in the waiting orders for the restaurant.
     * If change => refresh the display
     * If no change => Do nothing
     *
     */
    private boolean changeInWaitingOrders(){
        //Retrieve list from server
        ArrayList<OrderElement> waitingOrdersFromServe = DataManager.getWaitingOrdersForARestaurantOwner();

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
            currentWaitingOrdersObjectID.add(orderElement.getUserOrderInformationsID());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : waitingOrdersFromServe){
            if (currentWaitingOrdersObjectID.contains(orderElement.getUserOrderInformationsID())){
                checkSameList ++;
            }
        }

        if (waitingOrdersFromServe.size() != checkSameList){
            //Lists are not the same. Need to refresh
            waitingOrders = waitingOrdersFromServe;
            return true;
        }
        else {
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
            currentCurrentOrdersObjectID.add(orderElement.getUserOrderInformationsID());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : currentOrdersFromServe){
            if (currentCurrentOrdersObjectID.contains(orderElement.getUserOrderInformationsID())){
                checkSameList ++;
            }
        }

        if (currentOrdersFromServe.size() != checkSameList){
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

        objectIdHashMapForList = new HashMap<>();

        List<HashMap<String,String>> aList = new ArrayList<>();
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
            objectIdHashMapForList.put(i, order);
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
            objectIdHashMapForList.put(i, order);
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
                OrderElement order = objectIdHashMapForList.get(position+1);

                boolean isCurrent = false;
                if (currentOrders.contains(order)){
                    isCurrent = true;
                }

                goToDeliveryCommandDetail(order, isCurrent);
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



    /**
     * Display the waiting and the current orders on the Google Map.
     * Display each order via a marker. Red color for waiting orders, Green for current ones.
     */
    private void showOrdersOnMap(){
        mMap.clear();
        objectIdHashMapForMap = new HashMap<>();
        int objectIdHashMapIndex = 1;

        Location rlocation = DataManager.getUserLocation();
        LatLng rLatLng = new LatLng(rlocation.getLatitude(), rlocation.getLongitude());
        setCamera(rLatLng);


        for(OrderElement order : waitingOrders) {
            Location location = order.getDeliveryLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            String deliveryAddress = order.getDeliveryAddress();

            String markerTitle = getResources().getString(R.string.WaitingOrders) +" #"+ objectIdHashMapIndex;
            objectIdHashMapForMap.put(markerTitle, order);
            objectIdHashMapIndex++;

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(markerTitle)
                    .snippet(deliveryAddress)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        for(OrderElement order : currentOrders) {
            Location location = order.getDeliveryLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            String deliveryAddress = order.getDeliveryAddress();

            String markerTitle = getResources().getString(R.string.WaitingOrders) +" #"+ objectIdHashMapIndex;
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



    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToDeliveryCommandDetail(OrderElement order, boolean isCurrent) {
        if (DataManager.isStatusWaiting(order.getUserOrderInformationsID())){
            Orders.setActiveOrder(order);
            Intent intent = new Intent(this, DeliverCommandDetailActivity.class);
            intent.putExtra("isCurrent", isCurrent);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), getString(R.string.OrderNotAvailable),
                    Toast.LENGTH_SHORT).show();
            restartHandlerTimerForRefresh();
        }
    }

    /**
     * Restart the timer how deals with the refresh of the page.
     */
    public void restartHandlerTimerForRefresh(){
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(getMapRunnable(),0);
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
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}

