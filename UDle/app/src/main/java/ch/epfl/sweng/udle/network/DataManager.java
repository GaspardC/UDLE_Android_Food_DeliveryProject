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

/**
 *
 *  Created by rodri on 23/10/2015
 * This class manages the User's information relevant to the order, order status, and contact info
 * One user can currently have one order.
 * One order exist per user per time period so we create one Order Parse Object per DataManager
 * Instance.
 *
 */

public class DataManager {
    private static double maxDeliveryDistance;
    private static ParseUser user;
    private static ParseGeoPoint userLocation;
    private static ArrayList<OrderElement> pendingOrders;
    private static ArrayList<String> nearbyRestaurants;
    private static boolean isThereAnyNearbyRestaurants;

    private ParseUserOrderInformations userOrderInformations = null;

    public DataManager() {

        userOrderInformations = new ParseUserOrderInformations();
        user = userOrderInformations.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = ((double) (Integer)user.get("maxDeliveryDistanceKm"));
        //NEED AN OBJECT FOR RESTAURANTS AFTER USERS

    }


    /*
     * Find restaurants near the user
     */

    public static boolean getRestaurantLocationsNearTheUser() {

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
                    isThereAnyNearbyRestaurants = true;
                    user.put("ArrayOfNearRestaurant", nearbyRestaurants);
                    user.saveInBackground();
                } else {
                    // Something went wrong.
                }
            }
        });

        if (nearbyRestaurants == null || nearbyRestaurants.isEmpty()){
            isThereAnyNearbyRestaurants = false;
        }

        else {
            isThereAnyNearbyRestaurants = true;
        }

        return isThereAnyNearbyRestaurants;
    }


    /* Start a query of all the pending orders available within the restaurant's range.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     */

    public static ArrayList<OrderElement> getPendingOrdersForARestaurantOwner() throws ParseException {

        //Only restaurants can access this method
        if (user.get("RestaurantOwner") == false) {
            return null;
        }

        //Because this method is static, these fields may not be instantiated
        user = DataManager.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = (double) ((Integer) user.get("maxDeliveryDistance"));

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", "waiting for restaurant");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> OrderList, ParseException e) {
                if (e == null) {

                    //for all clients who are waiting the validation of their command
                    for (ParseObject userOrder : OrderList) {

                        //Cast to parseUserOrderInformations to use methods
                        ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                        //Get Client location - - - should be delivery location, not client's?
                        ParseUser client = parseUserOrder.getUser();
                        ParseGeoPoint clientLocation = client.getParseGeoPoint("currentLocation");
                        double distanceKm = userLocation.distanceInKilometersTo(clientLocation);

                        //We check if they are near of us (restaurant owner connected) if yes we
                        //concatinate a ArrayList of OrderElements and return
                        pendingOrders = new ArrayList<OrderElement>();

                        if (distanceKm <= maxDeliveryDistance) {
                            OrderElement order = parseUserOrder.getOrder();
                            pendingOrders.add(order);
                        }
                    }

                } else {
                    //failure of query
                    //THROW EXCEPTION OTHERWISE
                    pendingOrders = null;
                }
            }
        });

        if (nearbyRestaurants == null) {
            throw new ParseException(2, "Query search for pending orders failed");
        }

        return pendingOrders;
    }

    /*
     * Once order is created, the order doesn't appear on the server until an orderElement is
     * provided. If not, the data manager will be useful to a restaurant.
     *
     */
    public void pushOrderToServer(OrderElement orderElement){
        userOrderInformations.setOrder(orderElement);
        userOrderInformations.saveInBackground();
    }


    /*
     *  Change status of order to reflect that it's currenty being delivered
     */
    public void deliveryEnRoute(String deliveringRestaurant, String deliveryGuyNumber, int eta) {
        userOrderInformations.setDeliveryGuyNumber(deliveryGuyNumber);
        userOrderInformations.setParseDeliveringRestaurant(deliveringRestaurant);
        userOrderInformations.setExpectedTime(eta);
        userOrderInformations.setOrderStatus("enRoute");
        userOrderInformations.saveInBackground();
    }

    /*
     *  Change status of order to reflect that it's currenty delivered
     */
    public void deliveryDelivered() {
        if (userOrderInformations.getOrderStatus() == "enRoute") {
            userOrderInformations.setOrderStatus("delivered");
            userOrderInformations.saveInBackground();
        }
    }

    public static ParseUser getUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser;
    }

    public static ParseGeoPoint getUserLocation(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getParseGeoPoint("Location");
    }


}