package ch.epfl.sweng.udle.network;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;


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
    public static final double MAX_DISTANCE_IN_KM_TO_FIND_A_RESTAURANT = 10;
    public static ParseGeoPoint userLocation;
    private JSONObject pendingOrder;
    private ParseUserOrderInformations userOrderInformations;
    /** If we don't know the status (i.e. before login), it is set to 'Guest' */
    private UserStatus status = UserStatus.GUEST;

    public DataManager() {
    }

    public DataManager(double latitude, double longitude) {
        userOrderInformations = new ParseUserOrderInformations(latitude, longitude);
    }

    public void setUserLocation(double lat, double lon){
        ParseUser user = getCurrentParseUser();
        ParseGeoPoint point = new ParseGeoPoint(lat, lon);
        user.put("Location", point);
        user.saveInBackground();

        getUserLocation();
        setPendingOrdersForARestaurantOwner();

    }
    private void setUserStatus(UserStatus status){
        this.status = status;
    }
    private UserStatus getUserStatusRestaurantOwner() {
        return status;
    }

    public void getUserLocation(){
        ParseUser user = getCurrentParseUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                userLocation  = object.getParseGeoPoint("Location");
                // This will throw an exception, since the ParseUser is not authenticated
                object.saveInBackground();
                getRestaurantLocationsNearTheUser(userLocation);
            }
        });
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
    public void getRestaurantLocationsNearTheUser(ParseGeoPoint currLocation) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("RestaurantOwner", true);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    ArrayList<String> listAvailableRestaurant= new ArrayList<String>();

                    for(ParseUser restoUser : objects){
                        ParseGeoPoint locResto = restoUser.getParseGeoPoint("Location");
                        double km = locResto.distanceInKilometersTo(userLocation);
                        if(km<MAX_DISTANCE_IN_KM_TO_FIND_A_RESTAURANT){
                        listAvailableRestaurant.add(restoUser.getObjectId());
                        }
                    }
                    ParseUser user = getCurrentParseUser();
                    user.put("ArrayOfNearRestaurant", listAvailableRestaurant);
                    user.saveInBackground();
                } else {
                    // Something went wrong.
                }
            }
        });
    }
    public void getOrder(int userId, int restaurantId){
        //TODO
//    public void getRestaurantLocations(Location currLocation) {
//        //TODO
//    }

//    public OrderElement getOrder(int userId, int restaurantId){
//        return userOrderInformations.getOrder();
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


    public void setPendingOrdersForARestaurantOwner() {
        ParseUser user = getCurrentParseUser();
        /*Restaurant Owner only*/
//        if( (false.equals(user.get("RestaurantOwner")))){
//            return;
//        }
        final ParseGeoPoint locResto = user.getParseGeoPoint("Location");
        //In OrderInformation Class we check all orders that are still pending
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", "Pending");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> OrderList, ParseException e) {
                if (e == null) {

                    //for all clients who are waiting the validation of their command
                    for (ParseObject locOfClient : OrderList) {
                        ParseGeoPoint locClient = locOfClient.getParseGeoPoint("currentLocation");

                        double km = locResto.distanceInKilometersTo(locClient);
                        //We check if they are near of us (restaurant owner connected) if yes we add it to our PendingCommandForARestaurant column
                        if (km < MAX_DISTANCE_IN_KM_TO_FIND_A_RESTAURANT) {
                            pendingOrder = new JSONObject();

                            try {
                                pendingOrder.put("lat", locClient.getLatitude());
                                pendingOrder.put("lon", locClient.getLongitude());

                                pendingOrder.put("client", "idDuClient");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            getPendingOderForARestaurantOwner();
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            currentUser.put("PendingCommandForARestaurant", pendingOrder);
                            currentUser.saveInBackground();
                        }
                    }

                } else {
//                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }


    public void getOrdersForARestaurantOwner(){

    }

    public JSONObject getPendingOderForARestaurantOwner(){


            JSONObject p = pendingOrder;
            JSONObject j = new JSONObject();


        try {

            String client =  pendingOrder.getString("client");
            double lat =  pendingOrder.getDouble("lat");
            double lon =  pendingOrder.getDouble("lon");
            j.put("lat",lat);
            j.put("lon",lon);


        } catch (JSONException e) {
            e.printStackTrace();
        }

            return  j;

    }



}
