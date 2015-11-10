package ch.epfl.sweng.udle;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.network.DataManager;

/**
 * Created by skalli93 on 11/10/15.
 */
public class DataManagerTest {
    public OrderElement orderElement = new OrderElement();
    DataManager dataManager;
    //HOW DO WE SIMULATE A USER???

    //Simulate a situation where the order is created and is now ready to push onto a server.
    public void dataManagerUserTest(){
        dataManager = new DataManager();
        dataManager.getRestaurantLocationsNearTheUser();
        dataManager.pushOrderToServer(orderElement);
        dataManager.deliveryEnRoute("Holy Cow", "+419733893029", 20);
        dataManager.deliveryDelivered();

    }

    public void dataManagerNearbyRestaurants(){
        dataManager = new DataManager();
        ArrayList<String> nearbyRestaurants = dataManager.getRestaurantLocationsNearTheUser();
    }

    public void dataManagerRestaurantUser(){
        dataManager = new DataManager();

    }

}
