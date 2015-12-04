package ch.epfl.sweng.udle.network;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public ParseUserOrderInformations() {
        //A default constructor is required.
    }

    //Get current user
    public ParseUser getUser() {
        ParseUser user = (ParseUser) this.get("user");
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setUser(ParseUser value){
        this.put("user", value);
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
    public String getExpectedTime() {

        return  this.getString("expectedTime");
    }

    //Set the ETA for the food delivery
    public void setExpectedTime(int value) {

        if(value == -1){
            this.put("expectedTime", "-1");
        }
        else{
            Date d = new Date();

            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm, dd/MM");
            int time = (value* 60 * 1000);
            Date deliveryDate = new Date(d.getTime() + (time));

            String date = fmt.format(deliveryDate);
            this.put("expectedTime", date);
        }

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
    public void setOrder (ParseObject orderElement) {
       // this.put("orderElement", orderElement);
        this.put("orderElementPointer", orderElement);
        this.saveInBackground();
    }

    //Return Order in type ArrayListMenu
    public ParseObject getOrder(){
        ParseObject order = (ParseObject) this.get("orderElementPointer");
        try {
            order.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return order;
    }

}
