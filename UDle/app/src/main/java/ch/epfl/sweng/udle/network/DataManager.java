package ch.epfl.sweng.udle.network;


import android.location.Location;
import android.util.Log;

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
    private static ArrayList<OrderElement> currentOrders;
    private static ArrayList<String> nearbyRestaurants = new ArrayList<>();
    private static ParseUserOrderInformations userOrderInformations = null;


    /**
     * Create two new entries in the database:
     *    1st: Create a new entry in 'UserOrderInformations'. This entry is 'initialized', with Status=Waiting. Contains a pointer to the 2nd entry created (orderElement).
     *    2nd: Create a new entry in 'odrerElement' with the order in Orders.getActiveOrder(). This entry contains a pointer to the 1st entry, to be able to retrieve the 'actors' of the order later on.
     * This two entries are directly push to server.
     */
    public static void createNewParseUserOrderInformations(){
        userOrderInformations = new ParseUserOrderInformations();

        OrderElement orderElement = Orders.getActiveOrder();

        userOrderInformations.setUser(getUser());
        userOrderInformations.setParseDeliveringRestaurant("No restaurant assigned");
        userOrderInformations.setExpectedTime(-1);
        userOrderInformations.setOrderStatus(OrderStatus.WAITING.toString());
        userOrderInformations.setDeliveryGuyNumber("No number assigned");

        orderElement.setUserOrderInformationsID(userOrderInformations.getObjectId());
        ParseObject parseOrderElement = ParseOrderElement.createToServerAndGetId(orderElement);
        userOrderInformations.setOrder(parseOrderElement);

    }

    /**
     *  Return objectId array of nearby restaurants. Relies on user current position
     *  and position of the restaurant to determine if restaurant is suitable.
     *  Does not consider availability of restaurant.
     */
    public static void getRestaurantsNearTheUser() {
        user = DataManager.getUser();
        userLocation = user.getParseGeoPoint("Location");

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

                        double restaurantMaxDeliveryDistance = (double) ((Integer) restaurantUser.get("maxDeliveryDistanceKm"));
                        //Add to nearby restaurants list if they are within limits
                        if (distance <= restaurantMaxDeliveryDistance) {
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



    /**
     *  Change status of order to reflect that it's currenty being delivered.
     *
     */
    public static void deliveryEnRoute(String objectId, int eta) {
        ParseUser user = getUser();

        String deliveringRestaurant = user.getUsername();
        String deliveryGuyNumber = user.getString("phone");

        ParseUserOrderInformations userOrderInformations = getParseUserObjectWithId(objectId);
        userOrderInformations.setDeliveryGuyNumber(deliveryGuyNumber);
        userOrderInformations.setParseDeliveringRestaurant(deliveringRestaurant);
        userOrderInformations.setExpectedTime(eta);
        userOrderInformations.setOrderStatus(OrderStatus.ENROUTE.toString());
    }

    /**
     *  Change status of order to reflect that it's currenty delivered. Order is finish.
     */
    public static void deliveryDelivered(String objectId) {
        ParseUserOrderInformations userOrderInformations = getParseUserObjectWithId(objectId);
        userOrderInformations.setOrderStatus(OrderStatus.DELIVERED.toString());
    }

    /**
     *  Return current parse user
     */
    public static ParseUser getUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser;
    }

    /**
     *  Return current parse username
     */
    public static String getUserName(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getUsername();
    }

    /**
     *  Return user location
     */
    public static Location getUserLocation(){
        ParseUser currentUser = getUser();
        ParseGeoPoint parseGeoPoint = currentUser.getParseGeoPoint("Location");

        Location location = new Location("");
        location.setLatitude(parseGeoPoint.getLatitude());
        location.setLongitude(parseGeoPoint.getLongitude());

        return location;
    }

    /**
     *  Return current user status - user or restaurant
     */
    public static boolean isCurrentUserARestaurant(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getBoolean("RestaurantOwner");
    }


    /**
     * @param objectId String that identify a specif UserOrderInformations on the server
     * @return The ParseUserOrderInformations specify in the serve by the given objectId parameter.
     */
    private static ParseUserOrderInformations getParseUserObjectWithId(String objectId) {
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


    /**
     * @param objectId
     * @return Confirm that the current status of the order, specify by the objectId given as parameter, is 'Waiting'.
     */
    public static boolean isStatusWaiting(String objectId) {
        ParseUserOrderInformations parseUserOrderInformations = getParseUserObjectWithId(objectId);
        String orderStatus = parseUserOrderInformations.getOrderStatus();
        return orderStatus.equals(OrderStatus.WAITING.toString());
    }



    /**
     * Start a query of all the pending orders available within the restaurant's range.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getPendingOrdersForARestaurantOwner() {
        pendingOrders = new ArrayList<>();

        user = DataManager.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = (double) ((Integer) user.getNumber("maxDeliveryDistanceKm"));

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", OrderStatus.WAITING.toString());

        try {
            List<ParseObject> OrderList = query.find();

                for (ParseObject userOrder : OrderList) {

                //Cast to parseUserOrderInformations to use methods
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                ParseObject parseOrderElement = parseUserOrder.getOrder();
                OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(parseOrderElement);

                Location location = orderElement.getDeliveryLocation();
                ParseGeoPoint parseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                double distanceKm = userLocation.distanceInKilometersTo(parseLocation);

                //We check if they are near of us (restaurant owner connected) if yes we
                //concatinate a ArrayList of OrderElements and return

                if (distanceKm <= maxDeliveryDistance) {
                    pendingOrders.add(orderElement);
                }
            }
        }
        catch (Exception e) {
            //throw new IOException("Nhvvb");
        }
        return pendingOrders;
    }


    /**
     *  Start a query of all the enRoute orders that the restaurant accept.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getCurrentOrdersForARestaurantOwner() {
        currentOrders = new ArrayList<>();
        user = DataManager.getUser();

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("deliveringRestaurant", user.getUsername());
        query.whereEqualTo("orderStatus", OrderStatus.ENROUTE.toString());

        try {
            List<ParseObject> OrderList = query.find();

            for (ParseObject userOrder : OrderList) {
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                ParseObject parseOrderElement = parseUserOrder.getOrder();
                OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(parseOrderElement);
                currentOrders.add(orderElement);
            }
        }
        catch (Exception e) {
            //throw new IOException("Nhvvb");
        }
        return currentOrders;
    }
}