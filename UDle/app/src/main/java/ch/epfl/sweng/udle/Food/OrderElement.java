package ch.epfl.sweng.udle.Food;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 */
public class OrderElement {

    private ArrayList<Menu> orderList;
    private ArrayList<DrinkTypes> drinks;

    private Location deliveryLocation;
    private String deliveryAddress;



    public OrderElement(){
        this.orderList = new ArrayList<>();
        this.drinks = new ArrayList<>();
        this.deliveryLocation = null;
        this.deliveryAddress = "";
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
}
