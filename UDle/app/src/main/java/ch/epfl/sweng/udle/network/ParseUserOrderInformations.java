package ch.epfl.sweng.udle.network;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by skalli93 on 11/1/15.
 * This class is designed to abstract the parse user interface and communicate directly with the
 * parse server. Every getter and setter pair represents a column's methods in the parse server.
 */

@ParseClassName("ParseUserOrderInformations")
public class ParseUserOrderInformations extends ParseObject {

    /**
     * Default constructor is required by Parse.
     */
    public ParseUserOrderInformations() {
        //Empty
    }

    /**
     * @return User associated with this parseUserOrder
     */
    public ParseUser getUser() {
        ParseUser user = (ParseUser) this.get("user");
        if (user == null){
            throw new InternalError("Internal error: User retrieve is corrupted.");
        }
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @param newUser ParseUser who will receive the order.
     */
    public void setUser(ParseUser newUser){
        if (newUser == null){
            throw new IllegalArgumentException("Try to set user with a illegal value.");
        }
        this.put("user", newUser);
        this.saveInBackground();
    }

    /**
     * @return The contact info for the guy delivering the food
     */
    public String getDeliveryGuyNumber() {
        String number = this.getString("deliveryGuyNumber");
        if (number == null){
            throw new InternalError("Number is not assigined for this user.");
        }
        return number;
    }

    /**
     * @param newNumber The contact info for the guy delivering the food
     */
    public void setDeliveryGuyNumber(String newNumber) {
        if (newNumber == null){
            throw new IllegalArgumentException("Illegal string given for the delivery guy number.");
        }
        this.put("deliveryGuyNumber", newNumber);
        this.saveInBackground();
    }


    /**
     * @return the name of the restaurant delivering
     */
    public String getParseDeliveringRestaurant(){
        String restaurantName = this.getString("deliveringRestaurant");
        if (restaurantName == null){
            throw new InternalError("Delivering restaurant is not correctly set.");
        }
        return restaurantName;
    }

    /**
     * @param restaurantName The name of the restaurant delivering for this order.
     */
    public void setParseDeliveringRestaurantName(String restaurantName) {
        if (restaurantName == null  || restaurantName.equals("")){
            throw new IllegalArgumentException("Try ti set a wrong value for restaurant delivering.");
        }
        this.put("deliveringRestaurant", restaurantName);
        this.saveInBackground();
    }

    public void setDeliveringRestaurantId(){
        this.put("restaurantId",getUser().getObjectId());
        this.saveInBackground();
    }


    /**
     * @return The expected time (e.g 20:40, 12.04) of the order delivery
     */
    public String getExpectedTime() {
        String time = this.getString("expectedTime");
        if (time == null){
            throw new InternalError("The expected time cannot be retrieved.");
        }
        return  time;
    }


    /**
     * @param expectedTime Th expected time in minutes for the delivery
     */
    public void setExpectedTime(int expectedTime) {
        if(expectedTime == -1){
            this.put("expectedTime", "-1");
        }
        else{
            Date d = new Date();

            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm, dd/MM");
            int time = (expectedTime* 60 * 1000);
            Date deliveryDate = new Date(d.getTime() + (time));

            String date = fmt.format(deliveryDate);
            this.put("expectedTime", date);
        }

        this.saveInBackground();
    }


    /**
     * @return Current order status based on pre-defined strings
     */
    public String getOrderStatus() {
        String status = this.getString("orderStatus");
        if (status == null){
            throw new InternalError("Order Status is not set internally.");
        }
        return status;
    }

    /**
     * @param orderStatus String reflecting the actual status of the order
     */
    public void setOrderStatus(String orderStatus) {
        if (orderStatus == null || orderStatus.equals("")){
            throw new IllegalArgumentException("Try to set the order status to a wrong value.");
        }
        this.put("orderStatus", orderStatus);
        this.saveInBackground();
    }


    /**
     * @return The order corresponding with this userOrderInformatins as a ParseObject. Still Need to convert it into an OrderElement
     */
    public ParseObject getOrder(){
        try {
            this.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseObject order = (ParseObject) this.get("orderElementPointer");

        if (order == null){
            throw new InternalError("Order is not set correctly for this userOrder.");
        }
        try {
            order.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return order;
    }


    /**
     * @param orderElement ParseObject representing the orderElement of this userOrderInformations.
     */
    public void setOrder (ParseObject orderElement) {
        if (orderElement == null){
            throw new IllegalArgumentException("Try to set the order to a invalidl value");
        }
        this.put("orderElementPointer", orderElement);
        this.saveInBackground();
    }
}
