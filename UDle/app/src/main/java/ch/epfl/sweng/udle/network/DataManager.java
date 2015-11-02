package ch.epfl.sweng.udle.network;

import android.location.Location;
import android.util.Log;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.OrderElement;


/**
 * Created by rodri on 23/10/2015.
 */

public class DataManager {
    public static final double MAX_DISTANCE_IN_KM_TO_FIND_A_RESTAURANT = 10;
    public ParseGeoPoint userLocation;
    public   ArrayList<ParseUser> listAvailableRestaurant;

    public ParseUser getCurrentParseUser() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null)) {
            return currentUser;
        }
        else return null;
    }

    public void setUserLocation(double lat, double lon){
        ParseUser user = getCurrentParseUser();
        ParseGeoPoint point = new ParseGeoPoint(lat, lon);
        user.put("Location", point);
        user.saveInBackground();

        getUserLocation();

    }
    public void getUserLocation(){
        ParseUser user = getCurrentParseUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                userLocation  = object.getParseGeoPoint("Location");
                // This will throw an exception, since the ParseUser is not authenticated
                object.saveInBackground();
                getRestaurantLocations(userLocation);
            }
        });

    }


    //info all restaurant around the user location
    //get all restaurant locations, return all restaurant in a perimeter of 5km
    public void getRestaurantLocations(ParseGeoPoint currLocation) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("RestaurantOwner", "YES");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    Log.d("obj", objects.toString());
                    listAvailableRestaurant= new ArrayList<ParseUser>();

                    for(ParseUser restoUser : objects){
                        ParseGeoPoint locResto = restoUser.getParseGeoPoint("Location");
                        double km = locResto.distanceInKilometersTo(userLocation);
                        if(km<MAX_DISTANCE_IN_KM_TO_FIND_A_RESTAURANT){
                        listAvailableRestaurant.add(restoUser);
                        }

                    }
                } else {
                    // Something went wrong.
                }
            }
        });
    }
    public void getOrder(int userId, int restaurantId){
        //TODO
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
    public String getDeliveryGuyNumber(){
        //TODO
        return "";
    }
    public void setDeliveryGuyNumber(){
        //TODO
    }


    public void getOrdersForARestaurantOwner(){

    }
    public void setUserStatusRestaurantOwner(){

    }


}
