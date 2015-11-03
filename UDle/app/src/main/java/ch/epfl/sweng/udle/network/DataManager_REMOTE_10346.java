package ch.epfl.sweng.udle.network;

import android.location.Location;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import ch.epfl.sweng.udle.Food.OrderElement;


/**
 * Created by rodri on 23/10/2015.
 * This class manages the User's information relevant to the order, order status, and contact info
 * One user can currently have one order.
 *
 *
 * THESE METHODS NEED TO BE MADE STATIC. I AM HAVING TROUBLE FINDING A WAY TO GET THE EXACT
 * PARSE OBJECT FOR USER ORDER INFORMATION BASED ON JUST THE USER ID AND RESTAURANT ID
 *
 */
public class DataManager {
    private ParseUserOrderInformations userOrderInformations;

    public DataManager(double latitude, double longitude) {
        userOrderInformations = new ParseUserOrderInformations(latitude, longitude);
    }

    public ParseUser getCurrentParseUser() {
        return ParseUser.getCurrentUser();
    }

    public static void setUserLocation(ParseUser user, double latitude, double longitude){
        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        user.put("Location", point);
        user.saveInBackground();
    }

    //Info all restaurant around the user location
    //get all restaurant locations, return all restaurant in a perimeter of 5km
    public void getRestaurantLocations(Location currLocation) {
        //TODO
    }

    public OrderElement getOrder(int userId, int restaurantId){
        return userOrderInformations.getOrder();
    }

    public void setOrder(ParseUser userId, String restaurantId, OrderElement orderElement){
        userOrderInformations.setOrder(orderElement);
    }

    public String getOrderStatus(){
        return userOrderInformations.getOrderStatus();
    }

    public void setOrderStatus(String orderStatus){
        userOrderInformations.setOrderStatus(orderStatus);
    }

    public String getDeliveryGuyNumber(){
        return userOrderInformations.getDeliveryGuyNumber();
    }

    public void setDeliveryGuyNumber(String number){
        userOrderInformations.setDeliveryGuyNumber(number);
    }

    /*
    public void getOrdersForARestaurantOwner(){

    }
    public void setUserStatusRestaurantOwner(){

    }
    */


}
