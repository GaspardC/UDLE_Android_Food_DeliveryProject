package ch.epfl.sweng.udle.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.chooser.ChooserTargetService;
import android.support.v4.app.NotificationCompat;
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
import ch.epfl.sweng.udle.network.DataManager;

/**
 * Based on DeliveryRestaurantMapActivity's code.
 */
public class CurrentOrdersActivity extends SlideMenuActivity {

    private ListView listView;
    private HashMap<Integer, OrderElement> objectIdHashMapForList; //HashMap between the index of order shown in the list view and the specific order.
    Handler handler; //The list of waiting and current Orders is refresh each 'delay' milliseconds.
    final int delay = 30000; //30 seconds in milliseconds
    private ArrayList<OrderElement> waitingOrders = new ArrayList<>(); //Orders in the restaurant range that have no restaurant assigned to. Status of order: Waiting
    private ArrayList<OrderElement> currentOrders = new ArrayList<>(); //Orders that the restaurant already accept to deliverd. Status of order: EnRoute
    private ProgressDialog progress;
    private boolean waitingOrdersChange;
    private boolean currentOrdersChange;
    private boolean activityNotOnScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        handler =  new Handler(){
            public void handleMessage(Message msg)
            {
                // To dismiss the dialog
                progress.dismiss();
                if (waitingOrdersChange || currentOrdersChange) {
                    setUpListView();
                    if (currentOrdersChange){
                        if (activityNotOnScreen){
                            displayNotification();
                        }
                    }
                }
            }
        };
        handler.postDelayed(getMapRunnable(), 0);


        setUpListView();

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityNotOnScreen = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityNotOnScreen = false;
    }

    private void displayNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo2)
                        .setContentTitle(getString(R.string.notificationTitle))
                        .setContentText(getString(R.string.notificationText));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, CurrentOrdersActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(CurrentOrdersActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    /**
     * @return Runnable: Each 'delay' milliseconds, call the function run function. This run function check if orders need to be refresh and do it if needed.
     */
    private Runnable getMapRunnable(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {



                new Thread() {

                    @Override
                    public void run() {
                         waitingOrdersChange = changeInWaitingOrders();
                         currentOrdersChange = changeInCurrentOrders();

                        handler.sendEmptyMessage(0); // interact with UI
                    };

            }.start();

                handler.postDelayed(this, delay);
                if (!activityNotOnScreen){
                    progress = new ProgressDialog(CurrentOrdersActivity.this);
                    progress.setTitle(getResources().getString(R.string.Loading));
                    progress.setMessage(getString(R.string.waitWhileLoading));
                    progress.show();
                }

            }
        };
        return runnable;
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
        ArrayList<OrderElement> currentOrdersFromServe = DataManager.getEnRouteOrdersForAClient();

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
            listView = (ListView) findViewById(R.id.listCurrentOrders);
            populateListView();
        }

    /**
     * method used to fill (and display) the current and waiting orders
     */
    private void populateListView() {

        objectIdHashMapForList = new HashMap<>();

        List<HashMap<String,String>> aList = new ArrayList<>();
        ArrayList<String> ordersAdress = new ArrayList<>();
        int i = 1;

        if(waitingOrders!=null){
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
                objectIdHashMapForList.put(i,order);
                i++;
            }
        }
        if(currentOrders!=null){
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
                objectIdHashMapForList.put(i,order);
                i++;
            }
        }



        if(aList.isEmpty()){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("numCommande", getString(R.string.No_orders_for_now) + " ");
            hm.put("address",getString(R.string.Wait_a_moment));
            hm.put("image", Integer.toString(R.drawable.burger) );
            aList.add(hm);
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
     *
     * Check if there was a change in the waiting orders for the restaurant.
     * If change => refresh the display
     * If no change => Do nothing
     *
     */
    private boolean changeInWaitingOrders(){
        //Retrieve list from server
        ArrayList<OrderElement> waitingOrdersFromServe = DataManager.getWaitingOrdersForAClient();

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
    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToDeliveryCommandDetail(OrderElement order, boolean isCurrent) {
        Orders.setActiveOrder(order);

        Intent intent = new Intent(this, RecapActivity.class);
        intent.putExtra("from", "Current");
        startActivity(intent);
    }
}