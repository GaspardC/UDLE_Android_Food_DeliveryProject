package ch.epfl.sweng.udle.network;

import android.location.Location;


import com.parse.ParseUser;
import ch.epfl.sweng.udle.Food.OrderElement;


/**
 * Created by rodri on 23/10/2015.
 */
public class DataManager {

    public ParseUser getCurrentParseUser() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null)) {
            return currentUser;
        }
        else return null;
    }



    //info all restaurant around the user location
    //get all restaurant locations, return all restaurant in a perimeter of 5km
    public void getRestaurantLocations(Location currLocation){
        //TODO
    }

    public Order getOrder(int userId, int restaurantId){
        //TODO
        return null;
    }

    public void setOrder(int userId, int restaurantId, OrderElement orderElement){
        //TODO
    }

    public void getOrderStatus(){
        //TODO
    }

    public void setOrderStatus(){
        //TODO
    }

    public static String getDeliveryGuyNumber(){
        //TODO
        return "+41766796729";
    }

    public void setDeliveryGuyNumber(String phoneNumber){
        //TODO
    }


    public void getOrdersForARestaurantOwner(){

    }
    public void setUserStatusRestaurantOwner(){

    }


}
