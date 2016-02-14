package ch.epfl.sweng.udle.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;
import ch.epfl.sweng.udle.network.MyService;
import ch.epfl.sweng.udle.network.ParseApplication;

/**
 * Based on DeliveryRestaurantMapActivity's code.
 */
public class CurrentOrdersActivity extends SlideMenuActivity {

    private ListView listView;
    private HashMap<Integer, OrderElement> objectIdHashMapForList; //HashMap between the index of order shown in the list view and the specific order.
    Handler handler; //The list of waiting and current Orders is refresh each 'delay' milliseconds.
    final int delay = 60000; //60 seconds in milliseconds
    private ArrayList<OrderElement> waitingOrders = new ArrayList<>(); //Orders in the restaurant range that have no restaurant assigned to. Status of order: Waiting
    private ArrayList<OrderElement> currentOrders = new ArrayList<>(); //Orders that the restaurant already accept to deliverd. Status of order: EnRoute
    private ArrayList<OrderElement> deliveredOrders = new ArrayList<>();
    private ProgressDialog progress;
    private boolean waitingOrdersChange;
    private boolean currentOrdersChange;
    private boolean deliveredOrdersChange;
    private boolean activityNotOnScreen;
    private int timeLeftForRefresh;
    private Handler handlerRefresh = new Handler();
    private Button refreshButton;
    private boolean firstCreation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        refreshButton = (Button) findViewById(R.id.CurrentOrders_button_refresh);
        /*stopService();
        startService();*/
/*

        stopAlarm();
        startAlarm();
*/



        handler =  new Handler(){
            public void handleMessage(Message msg)
            {
                // To dismiss the dialog
                progress.dismiss();
                refreshButton.setEnabled(true);
                handlerRefresh.removeCallbacksAndMessages(null);
                if (waitingOrdersChange || currentOrdersChange) {
                    setUpListView();
/*                    if (changeInWaitingOrders()){
                        if (activityNotOnScreen){
                            displayNotification();
                        }
                    }*/
                }
                timeLeftForRefresh = delay/1000;
                handlerRefresh.postDelayed(getRefreshRunnable(), 0);
            }
        };
        handler.postDelayed(getMapRunnable(), 0);



        setUpListView();

    }

    private void stopAlarm() {
        Intent intent = new Intent(this, CurrentOrdersActivity.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);
    }

    private void startAlarm() {
        scheduleAlarm();

    }
    public void scheduleAlarm()
    {
        Calendar cal = Calendar.getInstance();

        Intent intent = new Intent(this, MyService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
// schedule for every 30 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10 * 1000, pintent);

//        Toast.makeText(this, "Alarm Scheduled", Toast.LENGTH_LONG).show();

    }

    // Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
    /**
     * @return Runnable who takes care of changing the text on the 'Refresh' button
     */
    private Runnable getRefreshRunnable(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String timeLeft = String.valueOf(timeLeftForRefresh);
                String textRefresh = getResources().getString(R.string.Refresh) + " (00:" + timeLeft + ")";
                refreshButton.setText(textRefresh);
                timeLeftForRefresh -= 1;
                handlerRefresh.postDelayed(this, 1000);
            }
        };
        return runnable;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        handler.removeCallbacksAndMessages(null);
        ParseApplication.currentActivityOnScreen = false;
        activityNotOnScreen = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseApplication.currentActivityOnScreen = true;
        activityNotOnScreen = false;
    }

    private void displayNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logoburger)
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
                         deliveredOrdersChange = changeInDeliveredOrders();

                        handler.sendEmptyMessage(0); // interact with UI
                    };

            }.start();

                handler.postDelayed(this, delay);
                if (!activityNotOnScreen){
                    progress = new ProgressDialog(CurrentOrdersActivity.this);
                    progress.setTitle(getResources().getString(R.string.Loading));
                    progress.setMessage(getString(R.string.waitWhileLoading));

                    if(!firstCreation){
                        progress.show();
                    }
                    firstCreation = false;
                }

            }
        };
        return runnable;
    }

    private boolean changeInDeliveredOrders() {
        //Retrieve list from server
        ArrayList<OrderElement> deliveredOrdersFromServe = DataManager.getDeliveredOrdersForAClient();

        if (deliveredOrdersFromServe.size() == 0){
            deliveredOrders = deliveredOrdersFromServe;
            return true;
        }

        //In order to check if there was a change or not, we compare the objectID of the list retrieve from server and the one already on device.
        //So use this int to see if the retrieved list is the same or not has the local one.
        int checkSameList = 0;

        //Putting all objectIds of local waitingOrders list into a new list
        ArrayList<String> deliveredOrdersObjectID = new ArrayList<>();
        for (OrderElement orderElement: deliveredOrders){
            deliveredOrdersObjectID.add(orderElement.getUserOrderInformationsID());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : deliveredOrdersFromServe){
            if (deliveredOrdersObjectID.contains(orderElement.getUserOrderInformationsID())){
                checkSameList ++;
            }
        }


        if (deliveredOrdersFromServe.size() != checkSameList){
            //Lists are not the same. Need to refresh
            deliveredOrders = deliveredOrdersFromServe;
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
        ArrayList<OrderElement> currentOrdersFromServe = DataManager.getEnRouteOrdersForAClient();

        if (currentOrdersFromServe.size() == 0){
            currentOrders = currentOrdersFromServe;

            return true;
        }

        //In order to check if there was a change or not, we compare the objectID of the list retrieve from server and the one already on device.
        //So use this int to see if the retrieved list is the same or not has the local one.
        int checkSameList = 0;

        //Putting all objectIds of local waitingOrders list into a new list
        ArrayList<String> currentOrdersObjectID = new ArrayList<>();
        for (OrderElement orderElement: currentOrders){
            currentOrdersObjectID.add(orderElement.getUserOrderInformationsID());
        }

        //Check for all orderElements in the server list if it already present locally. If yes, add 1 to 'checkSameList'
        for (OrderElement orderElement : currentOrdersFromServe){
            if (currentOrdersObjectID.contains(orderElement.getUserOrderInformationsID())){
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
        ArrayList<String> orderAddress = new ArrayList<>();
        int i = 1;

        if(waitingOrders!=null){
            for(OrderElement order : waitingOrders) {
                String deliveryAddress = order.getDeliveryAddress();
                orderAddress.add(deliveryAddress);
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("numCommande", "#" + i+" ");
                hm.put("address", orderAddress.get(i - 1));
                hm.put("image", Integer.toString(R.drawable.logoburger));
                aList.add(hm);
                objectIdHashMapForList.put(i,order);
                i++;
            }
        }
        if(currentOrders!=null){
            for(OrderElement order : currentOrders) {
                String deliveryAddress = order.getDeliveryAddress();
                orderAddress.add(deliveryAddress);
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("numCommande", "#" + i+" ");
                hm.put("address", orderAddress.get(i - 1));
                hm.put("image", Integer.toString(R.drawable.logogreen) );
                aList.add(hm);
                objectIdHashMapForList.put(i,order);
                i++;
            }
        }
        if(deliveredOrders!=null){
            for(OrderElement order : deliveredOrders) {
                String deliveryAddress = order.getDeliveryAddress();
                orderAddress.add(deliveryAddress);
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("numCommande", "#" + i+" ");
                hm.put("address", orderAddress.get(i - 1));
                hm.put("image", Integer.toString(R.drawable.logosilver) );
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
        // R.layout.listView_layout defines the layout of each item_drink
        SimpleAdapter adapter = new SimpleAdapter(this, aList, R.layout.list_item_restaurant_delivery, from, to);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                OrderElement order = objectIdHashMapForList.get(position+1);

                boolean isDelivered = false;
                if (deliveredOrders.contains(order)){
                    isDelivered = true;
                }

                goToDeliveryCommandDetail(order, isDelivered);
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
    public boolean changeInWaitingOrders(){
        //Retrieve list from server
        ArrayList<OrderElement> waitingOrdersFromServe = DataManager.getWaitingOrdersForAClient();
        ParseApplication.waitingOrders = waitingOrdersFromServe;


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
    public void goToDeliveryCommandDetail(OrderElement order, boolean isDelivered) {
        Orders.setActiveOrder(order);

        Intent intent = new Intent(this, RecapActivity.class);
        if (isDelivered){
            intent.putExtra("from", "Delivered");
        }
        else{
            intent.putExtra("from", "Current");
        }
        startActivity(intent);
    }

    /* These to methods aer use only for testing */
    public void resetCurrentOrder(){
        this.currentOrders = new ArrayList<> ();
    }
    public void resetWaitingOrders(){
        this.waitingOrders = new ArrayList<> ();
    }
    public void setWaitingOrdersForTesting(ArrayList<OrderElement> orderElements){
        waitingOrders = orderElements;
    }

    /**
     * Called when user click on the 'Refresh' button
     */
    public void refreshAll(View view){
        restartHandlerTimerForRefresh();
        refreshButton.setEnabled(false);
    }

    /**
     * Restart the timer how deals with the refresh of the page.
     */
    public void restartHandlerTimerForRefresh(){
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(getMapRunnable(), 0);
    }


}