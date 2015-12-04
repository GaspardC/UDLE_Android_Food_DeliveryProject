package ch.epfl.sweng.udle.Food;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 */

public class OrderElement {

    private ArrayList<Menu> orderList = new ArrayList<>();
    private ArrayList<DrinkTypes> drinks = new ArrayList<>();

    private Location deliveryLocation = null;
    private String deliveryAddress = "";

    private String orderedBy = "";

    private String userOrderInformationsID = "";



    public OrderElement(){
    }


    public void empty(){
        this.orderList = new ArrayList<>();
        this.drinks = new ArrayList<>();
    }

    public void addMenu (Menu menu){
        orderList.add(menu);
    }

    public ArrayList<Menu> getOrder(){
        return orderList;
    }
    public void setOrderList (ArrayList<Menu> orderList){
        this.orderList = orderList;
    }

    public void setDeliveryLocation(Location location){
        if (location == null){
            throw new IllegalArgumentException("Location is null.");
        }
        this.deliveryLocation = location;
    }
    public Location getDeliveryLocation(){
        return deliveryLocation;
    }

    public void setDeliveryAddress(String address){
        if ("".equals(address) || address==null){
            throw new IllegalArgumentException("Address is empty or null.");
        }
        this.deliveryAddress = address;
    }
    public String getDeliveryAddress(){
        return deliveryAddress;
    }

    public ArrayList<DrinkTypes> getDrinks(){
        return drinks;
    }
    public void addToDrinks(DrinkTypes drink){
        if(drink == null){
            throw new IllegalArgumentException("Ty to add a null drink");
        }
        this.drinks.add(drink);
    }
    public void removeToDrinks(DrinkTypes drink){
        if(drink == null){
            throw new IllegalArgumentException("Ty to remove a null drink");
        }
        else if(this.drinks.size() != 0){
            this.drinks.remove(drink);
        }
    }

    public void removeToFood(Menu menu){
        if(menu == null){
            throw new IllegalArgumentException("Ty to remove a null menu");
        }
        else if(this.orderList.size() != 0){
            this.orderList.remove(menu);
        }
    }



    public double getTotalCost(){
        double cost = 2.00; //Delivery Cost

        for (Menu menu : orderList){
            cost += menu.getFood().getPrice();
        }
        for(DrinkTypes drinkTypes : drinks){
            cost += drinkTypes.getPrice();
        }

        return cost;
    }

    public String getOrderedUserName() {
        return orderedBy;
    }

    public void setOrderedUserName(String userName){
        this.orderedBy = userName;
    }

    public String getUserOrderInformationsID() {
        return userOrderInformationsID;
    }
    public void setUserOrderInformationsID(String value){
        this.userOrderInformationsID = value;
    }

}
