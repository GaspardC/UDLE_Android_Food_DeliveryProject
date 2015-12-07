package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;


/**
 * Created by rodri on 23/10/2015.
 * This class represents all orders of the user, specially the currentOne (The one display on screen)
 */
public class Orders {
;
    private static ArrayList<OrderElement> currentOrders = new ArrayList<>();
    private static OrderElement            activeOrder = null;
    private static String                  moneyDevise = " CHF";


    public Orders(){
        currentOrders = new ArrayList<>();
        activeOrder = null;
    }


    /**
     * @return The active order, the one to be manipulated and displayed on the screen
     */
    public static OrderElement getActiveOrder(){
        return activeOrder;
    }

    /**
     * @param orderElement The order to be the active one, the displayed one
     */
    public static void setActiveOrder(OrderElement orderElement){
        //ActiveOrder can be null

        activeOrder = orderElement;
    }

    /**
     * @return A list of orderElement which status is either waiting or enRoute
     */
    public static ArrayList<OrderElement> getCurrentOrders(){
        return currentOrders;
    }

    /**
     * @param orderElement The active order which will go to the currentOrder list
     */
    public static void activeOrderToCurrentOrder(OrderElement orderElement){
        if (activeOrder != orderElement){
            throw new IllegalArgumentException("The orderElement pass is not the activeOrder.");
        }
        activeOrder = null;
        currentOrders.add(orderElement);
    }


    /**
     * @return The money devise (e.g. CHF)
     */
    public static String getMoneyDevise(){
        return moneyDevise;
    }
}