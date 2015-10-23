package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;


/**
 * Created by rodri on 23/10/2015.
 */
public class Orders {

    private static ArrayList<OrderElement> oldOrders = new ArrayList<>();
    private static ArrayList<OrderElement> currentOrders = new ArrayList<>();
    private static OrderElement            activeOrder = null;



    public static OrderElement getActiveOrder(){
        return activeOrder;
    }
    public static void setActiveOrder(OrderElement orderElement){
        activeOrder = orderElement;
    }

    public static ArrayList<OrderElement> getCurrentOrders(){
        return currentOrders;
    }
    public static void activeOrderToCurrentOrder(OrderElement orderElement){
        if (activeOrder != orderElement){
            throw new IllegalArgumentException();
        }
        activeOrder = null;
        currentOrders.add(orderElement);
    }

    public static void currentOrderFinished(OrderElement orderElement){
        if (! currentOrders.contains(orderElement)){
            throw new IllegalArgumentException();
        }
        currentOrders.remove(orderElement);
        oldOrders.add(orderElement);
    }
    public static ArrayList<OrderElement> getOldOrders(){
        return oldOrders;
    }

}
