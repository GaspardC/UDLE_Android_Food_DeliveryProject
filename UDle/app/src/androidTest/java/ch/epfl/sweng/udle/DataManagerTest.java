package ch.epfl.sweng.udle;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.junit.Test;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.network.DataManager;
import ch.epfl.sweng.udle.network.ParseUserOrderInformations;

import static junit.framework.Assert.fail;

/**
 * Created by skalli93 on 11/10/15.
 */
public class DataManagerTest {
/*
    public void setupClientUser() {
        try {
            ParseObject.registerSubclass(ParseUserOrderInformations.class);
            ParseObject.registerSubclass(OrderElement.class);
            Parse.initialize(new LoginActivity(), "9owjl8GmUsbfyoKtXhd5hK7QX8CUJVfuAvSLNoaY", "xd6XKHd9NxLfzFPbHQ5xaMHVzU1gfeLen0qCyI4F");

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

    /*
    public void setupRestaurantUser() {
        try {
            ParseObject.registerSubclass(ParseUserOrderInformations.class);
            ParseObject.registerSubclass(OrderElement.class);
            Parse.initialize(this, "9owjl8GmUsbfyoKtXhd5hK7QX8CUJVfuAvSLNoaY", "xd6XKHd9NxLfzFPbHQ5xaMHVzU1gfeLen0qCyI4F");

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
/*
    //Simulate a situation where the order is created and is now ready to push onto a server.
    @Test
    public void dataManagerUserTest(){
        setupClientUser();

        //ParseUser.enableAutomaticUser();
        OrderElement orderElement = new OrderElement();
        orderElement.setDeliveryAddress("testLocation");

        DataManager.createNewParseUserOrderInformations();
        DataManager.pushOrderToServer(orderElement);


        //RestaurantSide
        DataManager.deliveryEnRoute("Holy Cow", "+419733893029", 20);
        DataManager.deliveryDelivered();
    }
*/
    /*
    @Test
    public void dataManagerRestaurantTest(){
        //setupRestaurantUser();
        ParseUser.enableAutomaticUser();
        ArrayList<OrderElement> pendingOrders = dataManager.getPendingOrdersForARestaurantOwner();

    }
    */
}
