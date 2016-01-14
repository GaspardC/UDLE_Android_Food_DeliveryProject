package ch.epfl.sweng.udle.network;


import android.location.Location;

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


    public static ArrayList<ParseUser> nearbyRestaurants;

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
        ParseUser currentUser = getUser();
        return currentUser.getUsername();
    }

    /**
     *  Return user location as stored in server
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
     * Set the location of the user in the server
     * @param location Location to store in the server
     */
    public static void setUserLocation(Location location) {
        ParseGeoPoint currentLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        ParseUser currentUser = getUser();
        currentUser.put("Location", currentLocation);
        currentUser.saveInBackground();
    }

    /**
     * @param id ObjectId of the ParseUserOrderInformation on the server
     * @return ExpectedTime enter by the restaurant for the specified object.
     */
    public static String getExpectedTime(String id){
        ParseUserOrderInformations parseOrder = getParseUserObjectWithId(id);
        return parseOrder.getExpectedTime();
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
     * Create two new entries in the database:
     *    1st: Create a new entry in 'UserOrderInformations'. This entry is 'initialized', with Status=Waiting. Contains a pointer to the 2nd entry created (orderElement).
     *    2nd: Create a new entry in 'odrerElement' with the order in Orders.getActiveOrder(). This entry contains a pointer to the 1st entry, to be able to retrieve the 'actors' of the order later on.
     * This two entries are directly push to server.
     */
    public static void createNewParseUserOrderInformations(){
        ParseUserOrderInformations userOrderInformations = new ParseUserOrderInformations();

        OrderElement orderElement = Orders.getActiveOrder();

        userOrderInformations.setUser(getUser());
        userOrderInformations.setParseDeliveringRestaurantName("No restaurant assigned");
//        userOrderInformations.setDeliveringRestaurantId();
        userOrderInformations.setExpectedTime(-1);
        userOrderInformations.setOrderStatus(OrderStatus.WAITING.toString());
        userOrderInformations.setDeliveryGuyNumber("No number assigned");
        String objectId = userOrderInformations.getObjectId();
        while (objectId == null){
            objectId = userOrderInformations.getObjectId();
        }
        orderElement.setUserOrderInformationsID(objectId);
        ParseObject parseOrderElement = ParseOrderElement.create(orderElement);
        parseOrderElement.saveInBackground();
        userOrderInformations.setOrder(parseOrderElement);
        Orders.setActiveOrder(orderElement);

    }


    /**
     * Change status of order to reflect that it's currenty being delivered.
     * @param objectId id of the ParseUserObject
     * @param eta ExpectedTime for the delivery
     */
    public static void deliveryEnRoute(String objectId, int eta) {
        ParseUser user = getUser();

        String deliveringRestaurant = user.getUsername();
        String deliveryGuyNumber = user.getString("phone");

        ParseUserOrderInformations userOrderInformations = getParseUserObjectWithId(objectId);
        userOrderInformations.setDeliveryGuyNumber(deliveryGuyNumber);
        userOrderInformations.setDeliveringRestaurantId(user.getObjectId());
        userOrderInformations.setParseDeliveringRestaurantName(deliveringRestaurant);
        userOrderInformations.setExpectedTime(eta);
        userOrderInformations.setOrderStatus(OrderStatus.ENROUTE.toString());
    }

    /**
     * Change status of order to reflect that it's currenty delivered. Order is finish.
     * @param objectId id of the ParseUserObject
     */
    public static void deliveryDelivered(String objectId) {
        ParseUserOrderInformations userOrderInformations = getParseUserObjectWithId(objectId);
        userOrderInformations.setOrderStatus(OrderStatus.DELIVERED.toString());
    }



    /**
     * @param objectId String that identify a specif UserOrderInformations on the server
     * @return The ParseUserOrderInformations specify in the server by the given objectId parameter.
     */
    public static ParseUserOrderInformations getParseUserObjectWithId(String objectId) {
        ParseUserOrderInformations parseUserOrderInformations;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("objectId",objectId);

        try {
            parseUserOrderInformations = (ParseUserOrderInformations) query.getFirst();
        }

        catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return parseUserOrderInformations;
    }



    /**
     *  Return true if a restaurant is near the user and store objectId array of nearby restaurants in server.
     *  Relies on user current position
     *  and position of the restaurant to determine if restaurant is suitable.
     *  Does not consider availability of restaurant.
     */
    public static boolean getRestaurantsNearTheUser() {
        ParseUser user = DataManager.getUser();
        ParseGeoPoint userLocation = user.getParseGeoPoint("Location");
        Boolean restaurantAvailable = false;
        ArrayList<String> nearbyRestaurantsId = new ArrayList<>();
        nearbyRestaurants = new ArrayList<>();


        //Start Query
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("RestaurantOwner", true);
        try {
            List<ParseUser> listOfRestaurants =  query.find();
            for (ParseUser restaurantUser : listOfRestaurants) {

                //Calculate distance between restaraunt and clients
                ParseGeoPoint restaurantLocation = restaurantUser.getParseGeoPoint("Location");
                double distance = restaurantLocation.distanceInKilometersTo(userLocation);

                double restaurantMaxDeliveryDistance = (double) ((Integer) restaurantUser.get("maxDeliveryDistanceKm"));
                //Add to nearby restaurants list if they are within limits
                if (distance <= restaurantMaxDeliveryDistance) {
                    nearbyRestaurantsId.add(restaurantUser.getObjectId());
                    nearbyRestaurants.add(restaurantUser);
                    restaurantAvailable = true;
                }

            }
            user.put("ArrayOfNearRestaurant", nearbyRestaurantsId);
            user.saveInBackground();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return restaurantAvailable;
    }


    /**
     * Start a query of all the waiting orders of the client.
     * Compile an arraylist of all the order element objects and return.
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getWaitingOrdersForAClient() {
        ArrayList<OrderElement> pendingOrders = new ArrayList<>();

        ParseUser user = DataManager.getUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", OrderStatus.WAITING.toString());
        String id = user.getObjectId();
        query.whereEqualTo("user", user);

        try {
            List<ParseObject> OrderList = query.find();

            for (ParseObject userOrder : OrderList) {

                //Cast to parseUserOrderInformations to use methods
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                ParseObject parseOrderElement = parseUserOrder.getOrder();
                OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(parseOrderElement);

                pendingOrders.add(orderElement);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return pendingOrders;
    }


    /**
     * Start a query of all the enRoute orders of the client.
     * Compile an arraylist of all the order element objects and return.
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getEnRouteOrdersForAClient() {
        ArrayList<OrderElement> deliveredOrders = new ArrayList<>();

        ParseUser user = DataManager.getUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", OrderStatus.ENROUTE.toString());
        query.whereEqualTo("user", user);

        try {
            List<ParseObject> OrderList = query.find();

            for (ParseObject userOrder : OrderList) {

                //Cast to parseUserOrderInformations to use methods
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                ParseObject parseOrderElement = parseUserOrder.getOrder();
                OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(parseOrderElement);

                deliveredOrders.add(orderElement);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return deliveredOrders;
    }


    /**
     * Start a query of all the enRoute orders of the client.
     * Compile an arraylist of all the order element objects and return.
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getDeliveredOrdersForAClient() {
        ArrayList<OrderElement> enRouteOrders = new ArrayList<>();

        ParseUser user = DataManager.getUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Start Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("orderStatus", OrderStatus.DELIVERED.toString());
        query.whereEqualTo("user", user);

        try {
            List<ParseObject> OrderList = query.find();

            for (ParseObject userOrder : OrderList) {

                //Cast to parseUserOrderInformations to use methods
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;

                ParseObject parseOrderElement = parseUserOrder.getOrder();
                OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(parseOrderElement);

                enRouteOrders.add(orderElement);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return enRouteOrders;
    }



    /*              RESTAURANT METHODS                       */


    /**
     * Start a query of all the pending orders available within the restaurant's range.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getWaitingOrdersForARestaurantOwner() {
        ArrayList<OrderElement> pendingOrders = new ArrayList<>();

        ParseUser user = DataManager.getUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseGeoPoint userLocation = user.getParseGeoPoint("Location");
        double maxDeliveryDistance = (double) ((Integer) user.getNumber("maxDeliveryDistanceKm"));

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
            throw new IllegalStateException(e);
        }

        return pendingOrders;
    }


    /**
     *  Start a query of all the enRoute orders that the restaurant accept.
     * Compile an arraylist of all the order element objects and return
     * Returns null if query fails.
     */
    public static ArrayList<OrderElement> getCurrentOrdersForARestaurantOwner() {
        ArrayList<OrderElement> currentOrders = new ArrayList<>();
        ParseUser user = DataManager.getUser();

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
            throw new IllegalStateException(e);
        }
        return currentOrders;
    }

    /**
     * @return True if the user is register as a Restautant
     */
    public static boolean isARestaurant(){
        if (ParseUser.getCurrentUser() == null){
            return false;
        }
        ParseUser user = DataManager.getUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user.getBoolean("RestaurantOwner");
    }



    public static String getCustomerId() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("customer");
        query.whereEqualTo("parent", getUser());
        query.orderByAscending("updatedAt");

        String customerID = null;

        try {
            List<ParseObject> customers = query.find();
//            ParseObject customer = query.getFirst();

            if(customers != null && !customers.isEmpty()){
                ParseObject customer = customers.get(0);
                 customerID = customer.getString("sCID");
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return customerID;
    }

    public static void saveLast4(String last4) {
        getUser().put("last4", last4);
        getUser().saveInBackground();
    }

    public static String getLast4() {
        return getUser().getString("last4");
    }

    public static ParseUserOrderInformations getParseUserObjectWithActiveOrder() {
        OrderElement activeOrder = Orders.getActiveOrder();
        return getParseUserObjectWithId(activeOrder.getUserOrderInformationsID());
    }

    public static String getRestaurantNameWithActiveOrder() {
        ParseUserOrderInformations orderInformations = getParseUserObjectWithActiveOrder();
        return orderInformations.getParseDeliveringRestaurant();
    }


    public static ParseUser getRestaurantUserWithActiveOrder() {
        ParseUserOrderInformations orderInformations = getParseUserObjectWithActiveOrder();
        String restaurantId = orderInformations.getString("restaurantId");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId",restaurantId);
        ParseUser restaurantUser = null;

        try {
         restaurantUser =  query.getFirst();
                }

        catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return restaurantUser;
    }

    public static void setRestaurantMarkWithActiveOrder(int mark){
        ParseUser restaurantUser = getRestaurantUserWithActiveOrder();

        ParseRestaurantMark parseRestaurantMark = getParseRestaurantMark(restaurantUser);
        int averageMark = parseRestaurantMark.getAverage();
        int numberMark = parseRestaurantMark.getNumberMarks();
        parseRestaurantMark.incrementMark();

        int newNb = numberMark + 1;
        int newMark = (averageMark/newNb) + (mark/newNb);

        parseRestaurantMark.setAverage(newMark);
        parseRestaurantMark.saveInBackground();


        getParseUserObjectWithActiveOrder().setRated(true);
    }

    private static ParseRestaurantMark getParseRestaurantMark(ParseUser restaurantUser) {
        ParseRestaurantMark parseRestaurantMark;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRestaurantMark");
        query.whereEqualTo("RestaurantUser", restaurantUser.getObjectId());

        try {
            parseRestaurantMark = (ParseRestaurantMark) query.getFirst();
        }

        catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return parseRestaurantMark;
    }


}