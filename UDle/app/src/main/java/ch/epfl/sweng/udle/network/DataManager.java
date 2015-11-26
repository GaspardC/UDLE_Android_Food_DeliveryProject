package ch.epfl.sweng.udle.network;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;

/**
 *
 *  Created by rodri on 23/10/2015
 * This class manages the User's information relevant to the order, order status, and contact info
 * One user can currently have one order.
 * One order exist per user per time period so we create one Order Parse Object per DataManager
 * Instance.
 *
 * Restaurants can also use DataManager to find pendingOrders
 *
 */

public class DataManager {
    private static double maxDeliveryDistance;
    private static ParseUser user;
    private static ParseGeoPoint userLocation;
    private static ArrayList<OrderElement> pendingOrders;
    private static ArrayList<String> nearbyRestaurants;
    private static ParseUserOrderInformations userOrderInformations = null;


    /*
     * Because dataManager is now a static class, we don't need to keep relying on the
     * constructor to be able to use the user end functionality. Having a separate function is
     * a lot cleaner and more efficient.
     *
     */
    public static void createNewParseUserOrderInformations(){
        //Intialize ParseUserOrderInformationFields
        userOrderInformations = new ParseUserOrderInformations();
        user = userOrderInformations.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = ((double) (Integer)user.get("maxDeliveryDistanceKm"));

        //Get objectId, set ojectId in orderElement, and save to server
        String orderElementId = userOrderInformations.getObjectId();
        OrderElement orderElement = Orders.getActiveOrder();
        orderElement.setObjectId(orderElementId);
        userOrderInformations.setOrder(orderElement);
    }

    /*
     *  Return objectId array of nearby restaurants. Relies on user current position
     *  and position of the position of the restaurant and the maximum preffered distance between
     *  them to determine if restaurant is suitable. Does not consider availability of restaurant.
     *
     *  WHEN I TRY TO RETURN ARRAYLIST<STRING> THE VALUE IS ALWAYS NULL???
     */
    public static void getRestaurantsNearTheUser() {

        //Because this method is static, these fields may not be instantiated
        user = DataManager.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = (double) ((Integer) user.get("maxDeliveryDistanceKm"));

        //Start Query
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("RestaurantOwner", true);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> listOfRestaurants, ParseException e) {
                if (e == null) {

                    // The query was successful.
                    nearbyRestaurants = new ArrayList<String>();
                    for (ParseUser restaurantUser : listOfRestaurants) {

                        //Calculate distance between restaraunt and clients
                        ParseGeoPoint restaurantLocation = restaurantUser.getParseGeoPoint("Location");
                        double distance = restaurantLocation.distanceInKilometersTo(userLocation);

                        //Add to nearby restaurants list if they are within limits
                        if (distance <= maxDeliveryDistance) {
                            nearbyRestaurants.add(restaurantUser.getObjectId());
                        }

                    }
                    user.put("ArrayOfNearRestaurant", nearbyRestaurants);
                    user.saveInBackground();

                } else {
                    // Something went wrong.
                }
            }
        });
    }


    /* Start a query of all the pending orders available within the restaurant's range.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     *
     * THIS FUNCTION RETURNS AN ID
     * getOrderElementWithId
     *
     */

    public static ArrayList<OrderElement> getPendingOrdersForARestaurantOwner() {

        //Because this method is static, these fields may not be instantiated
        user = DataManager.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = (double) ((Integer) user.get("maxDeliveryDistance"));

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", "waiting for restaurant");

        try {
            List<ParseObject> OrderList = query.find();
            for (ParseObject userOrder : OrderList) {

                //Cast to parseUserOrderInformations to use methods
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                //Get Client location - - - should be delivery location, not client's?
                ParseUser client = parseUserOrder.getUser();
                ParseGeoPoint clientLocation = client.getParseGeoPoint("currentLocation");
                double distanceKm = userLocation.distanceInKilometersTo(clientLocation);

                //We check if they are near of us (restaurant owner connected) if yes we
                //concatinate a ArrayList of OrderElements and return

                if (distanceKm <= maxDeliveryDistance) {
                    OrderElement orderElement = parseUserOrder.getOrder();
                    pendingOrders.add(orderElement);
                }
            }
        }

        catch (Exception e) {
            //throw new IOException("Nhvvb");
        }

        return pendingOrders;
    }

    /*
     *  Change status of order to reflect that it's currenty being delivered
     *
     *  THESE ARE FUNCTIONS USED
     */
    public static void deliveryEnRoute(String objectId, String deliveringRestaurant, String deliveryGuyNumber, int eta) {

            ParseUserOrderInformations userOrderInformations = getParseUserObjectWithId(objectId);
            userOrderInformations.setDeliveryGuyNumber(deliveryGuyNumber);
            userOrderInformations.setParseDeliveringRestaurant(deliveringRestaurant);
            userOrderInformations.setExpectedTime(eta);
            userOrderInformations.setOrderStatus("enRoute");
    }

    /*
     *  Change status of order to reflect that it's currenty delivered
     */
    public static void deliveryDelivered() {
        if (userOrderInformations != null) {
            if (userOrderInformations.getOrderStatus() == "enRoute") {
                userOrderInformations.setOrderStatus("delivered");
            }
        }
    }

    /*
     *  Return current parse user
     */
    public static ParseUser getUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser;
    }


    public static String getUserName(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getUsername();
    }

    /*
     *  Return current parse user location
     */

    public static ParseGeoPoint getUserLocation(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getParseGeoPoint("Location");
    }

    /*
     *  Return current user status - user or restaurant
     */

    public static boolean isCurrentUserARestaurant(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getBoolean("RestaurantOwner");
    }



    //GET ORDERINFORMATIONS WITH ID
    public static ParseUserOrderInformations getParseUserObjectWithId(String objectId) {
        ParseUserOrderInformations parseUserOrderInformations = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("objectId",objectId);

        try {
            parseUserOrderInformations = (ParseUserOrderInformations) query.getFirst();
        }

        catch (Exception e) {
            //throw new IOException("QUERY NOT WORKING");
        }

        return parseUserOrderInformations;
    }

    /*
     * Start a query in the forground that returns the first object that matches with the order
    * objectId.
    * Each order has it's own unique objectId assigned by Parse, so we assume that there are
    * no duplicates.
    *
    *
    * SHOULD NOT THROW IO EXCEPTION - - - Create a new exception
    */
    public static boolean isStatusWaitingForRestaurant(String objectId) {

        ParseUserOrderInformations parseUserOrderInformations = getParseUserObjectWithId(objectId);
        String orderStatus = parseUserOrderInformations.getOrderStatus();
        return orderStatus.equals("waiting for restaurant");
    }


}