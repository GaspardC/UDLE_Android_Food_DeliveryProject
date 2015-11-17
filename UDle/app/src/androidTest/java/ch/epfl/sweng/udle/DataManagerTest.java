package ch.epfl.sweng.udle;

import com.parse.ParseUser;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.network.DataManager;

/**
 * Created by skalli93 on 11/10/15.
 */
public class DataManagerTest {
    public OrderElement orderElement = new OrderElement();
    DataManager dataManager;


    /*
    public void setupClientUser() {
        try {
            ParseUser userTest = ParseUser.logIn("userTest", "12345678");
            String sessionToken = userTest.getSessionToken();
            ParseUser.become(sessionToken);
        }
        catch (ParseException e){
            fail("Unable to login as the user");
        }

        if (DataManager.getUser() == null){
            fail("User is null");
        }

    }

    public void setupRestaurantUser() {
        try {
            ParseUser userTest = ParseUser.logIn("I62hSyM8FF", "12345678");
            String sessionToken = userTest.getSessionToken();
            ParseUser.become(sessionToken);
        }
        catch (ParseException e){
            fail("Unable to login as the user");
        }

        if (DataManager.getUser() == null){
            fail("User is null");
        }

    }

*/

    //Simulate a situation where the order is created and is now ready to push onto a server.
    @Test
    public void dataManagerUserTest(){
        //setupClientUser();
        ParseUser.enableAutomaticUser();

        dataManager = new DataManager();
        dataManager.getRestaurantLocationsNearTheUser();
        dataManager.pushOrderToServer(orderElement);
        dataManager.deliveryEnRoute("Holy Cow", "+419733893029", 20);
        dataManager.deliveryDelivered();
    }

    @Test
    public void dataManagerRestaurantTest(){
        //setupRestaurantUser();
        ParseUser.enableAutomaticUser();
        ArrayList<OrderElement> pendingOrders = dataManager.getPendingOrdersForARestaurantOwner();

    }
}
