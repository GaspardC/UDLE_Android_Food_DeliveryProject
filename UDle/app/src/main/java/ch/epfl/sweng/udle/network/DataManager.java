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
 *  Created by rodri on 23/10/2015.
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

    private ParseUserOrderInformations userOrderInformations = null;

    public DataManager(String userType) {

        //NEED AN OBJECT FOR RESTAURANTS AFTER USERS
        userOrderInformations = new ParseUserOrderInformations();
        user = userOrderInformations.getUser();
        userLocation = user.getParseGeoPoint("Location");
        maxDeliveryDistance = (double) user.get("maxDeliveryDistance");
    }


    /* Find restaurants near the user
     *
     */

    public ArrayList<String> getRestaurantLocationsNearTheUser() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("RestaurantOwner", true);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> listOfRestaurants, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (ParseUser restaurantUser : listOfRestaurants) {

                        //Calculate distance between restaraunt and clients
                        ParseGeoPoint restaurantLocation = restaurantUser.getParseGeoPoint("Location");
                        double distance = restaurantLocation.distanceInKilometersTo(userLocation);

                        //Add to nearby restaurants list if they are within limits
                        nearbyRestaurants = new ArrayList<String>();
                        if (distance <= maxDeliveryDistance) {
                            nearbyRestaurants.add(restaurantUser.getObjectId());
                        }
                    }
                    user.put("ArrayOfNearRestaurant", nearbyRestaurants);
                    user.saveInBackground();
                } else {
                    // Something went wrong.
                    nearbyRestaurants = null;
                }
            }
        });
        return nearbyRestaurants;
    }


    /* Start a query of all the pending orders available within the restaurant's range.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     */
    public ArrayList<OrderElement> getPendingOrdersForARestaurantOwner() {

        //Only restaurants can access this method
        if (user.get("RestaurantOwner") == false) {
            return null;
        }

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
                    pendingOrders = null;
                }
            }
        });

        return pendingOrders;
    }
}