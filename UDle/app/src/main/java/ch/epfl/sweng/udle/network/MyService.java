package ch.epfl.sweng.udle.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.CurrentOrdersActivity;

/**
 * Created by Gasp on 31/01/16.
 */
public class MyService extends Service {

    private ArrayList<OrderElement> waitingOrders = new ArrayList<>(); //Orders in the restaurant range that have no restaurant assigned to. Status of order: Waiting
    Handler mHandler=new Handler();

/*
    private Handler handler;
*/

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
/*
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
*/

        try {
            runa();
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        for(int i=0;i<5;i++) {
            try {
                Thread.sleep(10000);
                if (changeInWaitingOrders()) {
                    displayNotification();

                    onDestroy();
                    return START_STICKY;

                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return START_STICKY;*/

/*        try {
            runa();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*handler =  new Handler(){
            public void handleMessage(Message msg)
            {
                if(changeInWaitingOrders()){
                    displayNotification();
                    onDestroy();
                }

            }
        };*/
        //...





        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void runa() throws Exception{
        mHandler.post(new Runnable() {
            public void run() {

                new Thread(new Runnable(){
                    public void run() {
                        // TODO Auto-generated method stub
                        for(int i=0;i<10;i++)
                        {
                            try {
                                Thread.sleep(10000);
                                if(changeInWaitingOrders()){
                                    displayNotification();
                                    onDestroy();
                                }


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //REST OF CODE HERE//
                        }

                    }
                }).start();

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

    private void displayNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_udle_pin)
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

}