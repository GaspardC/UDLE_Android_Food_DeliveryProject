package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 */
public class Order {

    private ArrayList<Menu> orderList;

    public Order(){
        this.orderList = new ArrayList<>();
    }



    public void addMenu (Menu menu){
        orderList.add(menu);
    }

    public ArrayList<Menu> getOrder(){
        return orderList;
    }



}
