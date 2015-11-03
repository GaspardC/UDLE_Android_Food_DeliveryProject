package ch.epfl.sweng.udle.network;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import ch.epfl.sweng.udle.Food.OrderElement;

/**
 * Created by skalli93 on 11/1/15.
 * This class is designed to abstract the parse user interface and communicate directly with the
 * parse server. Every getter and setter pair represents a column's methods in the parse server.
 *
 * There will be one Parse User Order Information per user per session.
 * Data manager will handle the different rows available of this class.
 *
 */

@ParseClassName("ParseUserOrderInformations")
public class ParseUserOrderInformations extends ParseObject {

    /* Contructor takes in the current location latitude and longitude and initializes user info.
     * USER info gathered from ParseUser Method
     */
    public ParseUserOrderInformations(double latitude, double longitude) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null)) {
            this.put("user", currentUser);
        }

        this.setDeliveryGuyNumber("");
        this.setOrderStatus("Pending");

        ParseGeoPoint currentLocation = new ParseGeoPoint(latitude, longitude);
        this.setCurrentLocation(currentLocation);
    }

    //Return the contact info for the guy delivering the food
    public String getDeliveryGuyNumber() {
        return getString("deliveryGuyNumber");
    }

    //Set the contact info for the guy delivering the food
    public void setDeliveryGuyNumber(String value) {
        this.put("deliveryGuyNumber", value);
    }

    //Get the geoPoint for currentLocation, must be preset
    public ParseGeoPoint getCurrentLocation() {
        return getParseGeoPoint("currentLocation");
    }

    // Change current location
    public void setCurrentLocation(ParseGeoPoint value) {
        this.put("currentLocation", value);
    }

    // Retrieve current order status based on pre-defined strings
    public String getOrderStatus() {
        return getString("orderStatus");
    }

    //Change Order Status when order is made
    public void setOrderStatus(String orderStatus) {
        this.put("orderStatus", orderStatus);
    }

    //Retrieve the order parse element and add parameters to the order
    public void setOrder (OrderElement orderElement){
        this.put("order", orderElement);
    }

    //Return Order in type ArrayListMenu
    public OrderElement getOrder(){
        return (OrderElement) this.get("order");
    }

}