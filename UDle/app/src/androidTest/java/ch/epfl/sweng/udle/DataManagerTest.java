package ch.epfl.sweng.udle;

import com.parse.ParseException;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.network.DataManager;

import static junit.framework.Assert.fail;

/**
 * Created by skalli93 on 11/10/15.
 */
public class DataManagerTest {
    public OrderElement orderElement = new OrderElement();
    DataManager dataManager;
    //HOW DO WE SIMULATE A USER???

    //Simulate a situation where the order is created and is now ready to push onto a server.
    @Test
    public void dataManagerUserTest(){
        try {
            dataManager = new DataManager();
            dataManager.getRestaurantLocationsNearTheUser();
            dataManager.pushOrderToServer(orderElement);
            dataManager.deliveryEnRoute("Holy Cow", "+419733893029", 20);
            dataManager.deliveryDelivered();

        }

        catch (ParseException e){
            fail("dataManagerUserTest failed");
        }
    }

    @Test
    public void dataManagerNearbyRestaurants(){
        dataManager = new DataManager();
        try {
            ArrayList<String> nearbyRestaurants = dataManager.getRestaurantLocationsNearTheUser();
        }
        catch (ParseException e){
            fail("dataManagerRestaurantTest failed");
        }
    }

}
