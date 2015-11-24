package ch.epfl.sweng.udle.network;

import com.parse.ParseClassName;
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

    /*
     * Contructor takes in the current location latitude and longitude and initializes user info.
     * USER info gathered from ParseUser Method
     *
     */

    public ParseUserOrderInformations() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        this.put("user", currentUser);
        this.setDeliveryGuyNumber("no number assigned");
        this.setOrderStatus("waiting for restaurant");
        this.setExpectedTime(0);
        this.setParseDeliveringRestaurant("no restaurant assigned");

    }

    //Get current user
    public ParseUser getUser() {

        return (ParseUser) this.get("user");
    }


    //Return the contact info for the guy delivering the food
    public String getDeliveryGuyNumber() {

        return this.getString("deliveryGuyNumber");
    }

    //Set the contact info for the guy delivering the food
    public void setDeliveryGuyNumber(String value) {

        this.put("deliveryGuyNumber", value);
        this.saveInBackground();
    }

    //Return the name of the restaurant delivering
    public String getParseDeliveringRestaurant(){

        return this.getString("deliveringRestaurant");
    }

    //Set the name of the restaurant delivering
    public void setParseDeliveringRestaurant(String value) {

        this.put("deliveringRestaurant", value);
        this.saveInBackground();
    }


    //Return the ETA of the food delivery
    public int getExpectedTime() {

        return (int) this.getNumber("expectedTime");
    }

    //Set the ETA for the food delivery
    public void setExpectedTime(int value) {

        this.put("expectedTime", value);
        this.saveInBackground();
    }


    // Retrieve current order status based on pre-defined strings
    public String getOrderStatus() {

        return this.getString("orderStatus");
    }

    //Change Order Status when order is made
    public void setOrderStatus(String orderStatus) {

        this.put("orderStatus", orderStatus);
        this.saveInBackground();
    }

    //Retrieve the order parse element and add parameters to the order
    public void setOrder (OrderElement orderElement){
        this.put("orderElement", orderElement);
        this.saveInBackground();
    }

    //Return Order in type ArrayListMenu
    public OrderElement getOrder(){

        return (OrderElement) this.get("orderElement");
    }

}
