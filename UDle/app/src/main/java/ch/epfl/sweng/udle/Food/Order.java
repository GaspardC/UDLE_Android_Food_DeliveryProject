package ch.epfl.sweng.udle.Food;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 */
public class Order {

    private ArrayList<Menu> orderList;

    private Location deliveryLocation;
    private String deliveryAddress;


    public Order(){
        this.orderList = new ArrayList<>();
        this.deliveryLocation = null;
        this.deliveryAddress = "";
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



}
