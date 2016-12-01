/*
package ch.epfl.sweng.udle;

import android.location.Location;

import org.junit.Test;


import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

*/
/**
 * Created by rodri on 30/10/2015.
 *//*

public class OrderElementTest {

    ArrayList<Menu> orderList = new ArrayList<>();
    ArrayList<DrinkTypes> drinks = new ArrayList<>();
    Location deliveryLocation = null;
    String deliveryAddress = "";

    public void addMenu(){
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);

        orderList.add(menu);
    }

    public ArrayList<DrinkTypes> getDrinks(){
        drinks.add(DrinkTypes.BEER);
        drinks.add(DrinkTypes.COCA);
        return drinks;
    }

    @Test
    public void initTest(){
        OrderElement orderElement = new OrderElement();

        assertEquals(0, orderElement.getMenus().size());
        assertEquals(0, orderElement.getDrinks().size());
        assertEquals(deliveryLocation, orderElement.getDeliveryLocation());
        assertEquals(deliveryAddress, orderElement.getDeliveryAddress());
    }

    @Test
    public void addMenuTest(){
        OrderElement orderElmement = new OrderElement();

        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElmement.addMenu(menu);

        addMenu();

        ArrayList<Menu> menus = orderElmement.getMenus();
        for (int i=0 ; i < menus.size() ; i++){
            Menu men = orderList.get(i);
            assertEquals(men.getFood().toString(), menus.get(i).getFood().toString());
            for(int j=0; j < men.getOptions().size(); j++){
                assertEquals(men.getOptions().get(j).toString(), menus.get(i).getOptions().get(j).toString());
            }
        }
    }

    @Test
    public void addTwoMenuTest(){
        OrderElement orderElmement = new OrderElement();

        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElmement.addMenu(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElmement.addMenu(menu1);

        addMenu();
        addMenu();

        ArrayList<Menu> menus = orderElmement.getMenus();
        for (int i=0 ; i < menus.size() ; i++){
            Menu men = orderList.get(i);
            assertEquals(men.getFood().toString(), menus.get(i).getFood().toString());
            for(int j=0; j < men.getOptions().size(); j++){
                assertEquals(men.getOptions().get(j).toString(), menus.get(i).getOptions().get(j).toString());
            }
        }
    }


    @Test
    public void deliveryLocation(){
        OrderElement orderElement = new OrderElement();
        Location location = new Location("");
        location.setLatitude(20.0111);
        location.setLongitude(55.234322);
        orderElement.setDeliveryLocation(location);

        assertEquals(20.0111, orderElement.getDeliveryLocation().getLatitude());
        assertEquals(55.234322, orderElement.getDeliveryLocation().getLongitude());
    }
    @Test
    public void nullDeliveryLocation(){
        OrderElement orderElement = new OrderElement();
        try {
            orderElement.setDeliveryLocation(null);
            fail("Null location doesn't thrown an exception");
        } catch (IllegalArgumentException e){
            //goood
        }
    }


    @Test
    public void correctDeliveryAddress(){
        OrderElement orderElement = new OrderElement();
        orderElement.setDeliveryAddress("Route de SwEng");
        assertEquals("Route de SwEng", orderElement.getDeliveryAddress());
    }
    @Test
    public void nullDeliveryAddress(){
        OrderElement orderElement = new OrderElement();
        try {
            orderElement.setDeliveryAddress(null);
            fail("Null delivery address doesn't thrown an exception");
        } catch (IllegalArgumentException e){
            //goood
        }
    }
    @Test
    public void emptyDeliveryAddress(){
        OrderElement orderElement = new OrderElement();
        try {
            orderElement.setDeliveryAddress("");
            fail("Empty delivery address doesn't thrown an exception");
        } catch (IllegalArgumentException e){
            //goood
        }
    }


    @Test
    public void correctDrinks(){
        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.COCA);

        assertEquals(getDrinks(), orderElement.getDrinks());
    }
    @Test
    public void nullDrink(){
        OrderElement orderElement = new OrderElement();
        try {
            orderElement.addToDrinks(null);
            fail("Null drink doesn't thrown an exception");
        } catch (IllegalArgumentException e){
            //goood
        }
    }
    @Test
    public void nullDrink2(){
        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.FANTA);
        try {
            orderElement.addToDrinks(null);
            fail("Null drink doesn't thrown an exception");
        } catch (IllegalArgumentException e){
            //goood
        }
    }

    @Test
    public void removeDrink(){
        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.WATER);
        orderElement.addToDrinks(DrinkTypes.WATER);
        orderElement.addToDrinks(DrinkTypes.WATER);
        orderElement.addToDrinks(DrinkTypes.WATER);

        orderElement.removeToDrinks(DrinkTypes.COCA);

        assertEquals(6, orderElement.getDrinks().size());
        assertEquals(false, orderElement.getDrinks().contains(DrinkTypes.COCA));
    }
    @Test
    public void removeDrinksNotIn(){
        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.removeToDrinks(DrinkTypes.FANTA);
        assertEquals(1, orderElement.getDrinks().size());
    }
    @Test
    public void removeDrinksNull(){
        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.BEER);
        try{
            orderElement.removeToDrinks(null);
            fail("Remove drinks null fail");
        } catch (IllegalArgumentException e){
            //Good
        }
    }


    @Test
    public void removeFood(){
        OrderElement orderElement = new OrderElement();
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        orderElement.addMenu(menu1);
        orderElement.addMenu(menu1);
        orderElement.removeToFood(menu1);

        assertEquals(1, orderElement.getMenus().size());
    }
    @Test
    public void removeFoodNotIn(){
        OrderElement orderElement = new OrderElement();
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        orderElement.addMenu(menu1);
        orderElement.addMenu(menu1);
        Menu menu2 = new Menu();
        menu2.setFood(FoodTypes.BURGER);
        orderElement.removeToFood(menu2);
        assertEquals(2, orderElement.getMenus().size());
    }
    @Test
    public void removeFoodNull(){
        OrderElement orderElement = new OrderElement();
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        orderElement.addMenu(menu1);
        try{
            orderElement.removeToFood(null);
            fail("Remove food null fail");
        } catch (IllegalArgumentException e){
            //Good
        }
    }

    @Test
    public void getUSerNameOrder(){
        OrderElement orderElement = new OrderElement();
        assertEquals("", orderElement.getOrderedUserName());
    }
    @Test
    public void setUserName(){
        OrderElement orderElement = new OrderElement();
        orderElement.setOrderedUserName("Test user name udle");
        assertEquals("Test user name udle", orderElement.getOrderedUserName());
    }
    @Test
    public void setuserNameNull(){
        OrderElement orderElement = new OrderElement();
        try{
            orderElement.setOrderedUserName(null);
            fail("Null username orders do error");
        } catch (IllegalArgumentException e){
            //Good
        }
    }
    @Test
    public void setuserNameEmpty(){
        OrderElement orderElement = new OrderElement();
        try{
            orderElement.setOrderedUserName("");
            fail("Empty username orders do error");
        } catch (IllegalArgumentException e){
            //Good
        }
    }



    @Test
    public void getUserOrderInfoId(){
        OrderElement orderElement = new OrderElement();
        assertEquals("", orderElement.getUserOrderInformationsID());
    }
    @Test
    public void setUserOrderInfoId(){
        OrderElement orderElement = new OrderElement();
        orderElement.setUserOrderInformationsID("111222555555");
        assertEquals("111222555555", orderElement.getUserOrderInformationsID());
    }
    @Test
    public void setUserOrderInfoIdNull(){
        OrderElement orderElement = new OrderElement();
        try{
            orderElement.setUserOrderInformationsID(null);
            fail("Null userOrderID orders do error");
        } catch (IllegalArgumentException e){
            //Good
        }
    }
    @Test
    public void setUserOrderInfoIdEmpty(){
        OrderElement orderElement = new OrderElement();
        try{
            orderElement.setUserOrderInformationsID("");
            fail("Empty userOrderID orders do error");
        } catch (IllegalArgumentException e){
            //Good
        }
    }


    @Test
    public void correctTotalCost(){
        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.WATER);

        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu1);

        Double cost = 2.00;
        cost += DrinkTypes.BEER.getPrice();
        cost += DrinkTypes.WATER.getPrice();
        cost += FoodTypes.KEBAB.getPrice();
        cost += FoodTypes.KEBAB.getPrice();

        assertEquals(cost, orderElement.getTotalCost());
    }

    @Test
    public void emptyOrderTotalCost(){
        OrderElement orderElement = new OrderElement();

        assertEquals(2.00, orderElement.getTotalCost());
    }
}*/
