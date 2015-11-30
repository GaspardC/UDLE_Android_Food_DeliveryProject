package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.model.LatLng;

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

/**
 * Based on DeliveryRestaurantMapActivity's code.
 */
public class CurrentOrdersActivity extends SlideMenuActivity {

    private ListView listView;
    final ArrayList<OrderElement> currentOrders = getCurentOrders(new ArrayList<OrderElement>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        setUpListView();
    }

    private void setUpListView() {
        listView = (ListView) findViewById(R.id.listCurrentOrders);
        listView.setVisibility(View.VISIBLE);
        populateListView();
    }
    private void populateListView() {
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        ArrayList<String> ordersAdress = new ArrayList<>();
        int i = 1;
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
                for (OrderElement order : currentOrders) {
                    if (order.getDeliveryAddress().equals(adress)) { //TODO: Instead of compare with the address, compare with the id of the command for example.
                        goToCurrentOrdersCommandDetail(order);
                    }
                }
            }
        });
    }
    public void goToCurrentOrdersCommandDetail(OrderElement order) {
        Orders.setActiveOrder(order);
        Intent intent = new Intent(this, CurrentOrdersDetailActivity.class);
        Orders.setActiveOrder(order);
        startActivity(intent);
    }
    /** JUST FOR TEST**/
    public ArrayList<OrderElement> getCurentOrders(ArrayList<OrderElement> orders){
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
        orderElement1.setOrderedUserName("User Name");
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
        orderElement2.setOrderedUserName("User Name2");
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
        orderElement3.setOrderedUserName("User Name3");
        orders.add(orderElement3);
        return orders;
    }
}