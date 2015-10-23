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
        this.deliveryLocation = null;
        this.deliveryAddress = "";
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
        this.deliveryLocation = location;
    }
    public Location getDeliveryLocation(){
        return deliveryLocation;
    }

    public void setDeliveryAddress(String address){
        this.deliveryAddress = address;
    }
    public String getDeliveryAddress(){
        return deliveryAddress;
    }

    public ArrayList<DrinkTypes> getDrinks(){
        return drinks;
    }
    public void addToDrinks(DrinkTypes drink){
        this.drinks.add(drink);
    }

}
