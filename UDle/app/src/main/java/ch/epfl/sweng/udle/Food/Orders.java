package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;


/**
 * Created by rodri on 23/10/2015.
 */
public class Orders {

    private static ArrayList<OrderElement> oldOrders = new ArrayList<>();
    private static ArrayList<OrderElement> currentOrders = new ArrayList<>();
    private static OrderElement            activeOrder = null;
    private static String                  moneyDevise = " CHF";


    public Orders(){
        oldOrders = new ArrayList<>();
        currentOrders = new ArrayList<>();
        activeOrder = null;
    }


    public static OrderElement getActiveOrder(){
        return activeOrder;
    }
    public static void setActiveOrder(OrderElement orderElement){
        //ActiveOrder can be null

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

    public static String getMoneyDevise(){
        return moneyDevise;
    }
}