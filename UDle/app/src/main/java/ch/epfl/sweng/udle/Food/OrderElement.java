package ch.epfl.sweng.udle.Food;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 *
 * This class represents an orderElement, which is composed of a menu, a list of drinks,
 * the location and the address of delivery and the username of who order this command.
 * Ther is also a field called 'userOderInformationsID to link to this class in parse.
 */

public class OrderElement {

    private ArrayList<Menu> orderList = new ArrayList<>();
    private ArrayList<DrinkTypes> drinks = new ArrayList<>();

    private Location deliveryLocation = null;
    private String deliveryAddress = "";

    private String orderedBy = "";

    private String userOrderInformationsID = "";


    /**
     * Parse requites an empty constructor
     */
    public OrderElement(){
        //Empty
    }


    /**
     * @param menu Add a menu to the order.
     */
    public void addMenu (Menu menu){
        if (menu.isEmpty()){
            throw new IllegalArgumentException("Invalid menu pass as parameter.");
        }
        orderList.add(menu);
    }

    /**
     * @return The olist of Menus
     */
    public ArrayList<Menu> getMenus(){
        return orderList;
    }

    /**
     * @param orderList The lis of menus for this order element.
     */
    public void setOrderList (ArrayList<Menu> orderList){
        this.orderList = orderList;
    }


    /**
     * @param location Location of the order delivery address.
     */
    public void setDeliveryLocation(Location location){
        if (location == null){
            throw new IllegalArgumentException("Location is null.");
        }
        this.deliveryLocation = location;
    }

    /**
     * @return The Location of the delivery address
     */
    public Location getDeliveryLocation(){
        return deliveryLocation;
    }

    /**
     * @param address The address where the delivery guy will go
     */
    public void setDeliveryAddress(String address){
        if ("".equals(address) || address==null){
            throw new IllegalArgumentException("Address is empty or null.");
        }
        this.deliveryAddress = address;
    }

    /**
     * @return Set the delivery address for this order element.
     */
    public String getDeliveryAddress(){
        return deliveryAddress;
    }


    /**
     * @return List of all drinks in this order element.
     */
    public ArrayList<DrinkTypes> getDrinks(){
        return drinks;
    }

    /**
     * @param drink Drink to add into the list of drinks for this order element.
     */
    public void addToDrinks(DrinkTypes drink){
        if(drink == null){
            throw new IllegalArgumentException("Ty to add a null drink");
        }
        this.drinks.add(drink);
    }

    /**
     * @param drink Drink to remove from the list of drinks for this order element.
     */
    public void removeToDrinks(DrinkTypes drink){
        if(drink == null){
            throw new IllegalArgumentException("Ty to remove a null drink");
        }
        else if(this.drinks.size() != 0){
            this.drinks.remove(drink);
        }
    }


    /**
     * @param menu Menu to remove from the list of menus for this order element.
     */
    public void removeToFood(Menu menu){
        if(menu == null){
            throw new IllegalArgumentException("Ty to remove a null menu");
        }
        else if(this.orderList.size() != 0){
            this.orderList.remove(menu);
        }
    }


    /**
     * @return The total cost of the order (Menus + Drinks)
     */
    public double getTotalCost(){
        double cost = 0.00; //Delivery Cost

        for (Menu menu : orderList){
            cost += menu.getFood().getPrice();
        }
        for(DrinkTypes drinkTypes : drinks){
            cost += drinkTypes.getPrice();
        }

        return cost;
    }

    /**
     * @return The username of the client for this order
     */
    public String getOrderedUserName() {
        return orderedBy;
    }

    /**
     * @param userName The username of the client for this order
     */
    public void setOrderedUserName(String userName){
        if (userName == null  || userName.equals("")){
            throw new IllegalArgumentException("Invalid userName");
        }
        this.orderedBy = userName;
    }

    /**
     * @return The objectId of Parse corresponding to the userOrderInformations of this order element
     */
    public String getUserOrderInformationsID() {
        return userOrderInformationsID;
    }

    /**
     * @param value The objectId of Parse corresponding to the userOrderInformations of this order element
     */
    public void setUserOrderInformationsID(String value){
        if(value == null  || value.equals("")){
            throw new IllegalArgumentException("Invalid id");
        }
        this.userOrderInformationsID = value;
    }

}
