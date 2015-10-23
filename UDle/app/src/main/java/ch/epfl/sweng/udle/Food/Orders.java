package ch.epfl.sweng.udle.Food;

import java.security.InvalidParameterException;
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
        if (orderElement == null){
            throw new IllegalArgumentException("Try to set the activeOrder to a null object");
        }
        activeOrder = orderElement;
    }

    public static ArrayList<OrderElement> getCurrentOrders(){
        return currentOrders;
    }
    public static void activeOrderToCurrentOrder(OrderElement orderElement){
        if (activeOrder != orderElement){
            throw new IllegalArgumentException("The orderElement pass is not the activeOrder.");
        }
        activeOrder = null;
        currentOrders.add(orderElement);
    }

    public static void currentOrderFinished(OrderElement orderElement){
        if (! currentOrders.contains(orderElement)){
            throw new IllegalArgumentException("The orderElement pass is not present in the currentOrder list.");
        }
        currentOrders.remove(orderElement);
        oldOrders.add(orderElement);
    }
    public static ArrayList<OrderElement> getOldOrders(){
        return oldOrders;
    }

}
