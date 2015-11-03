package ch.epfl.sweng.udle.Food;

import android.location.Location;

import org.junit.Test;


import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by rodri on 30/10/2015.
 */
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

        assertEquals(orderList, orderElement.getOrder());
        assertEquals(drinks, orderElement.getDrinks());
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

        ArrayList<Menu> menus = orderElmement.getOrder();
        for (int i=0 ; i < menus.size() ; i++){
            Menu men = orderList.get(i);
            assertEquals(men.getFood(), menus.get(i).getFood());
            assertEquals(men.getOptions(), menus.get(i).getOptions());
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

        ArrayList<Menu> menus = orderElmement.getOrder();
        for (int i=0 ; i < menus.size() ; i++){
            Menu men = orderList.get(i);
            assertEquals(men.getFood(), menus.get(i).getFood());
            assertEquals(men.getOptions(), menus.get(i).getOptions());
        }
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
        orderElement.addToDrinks(DrinkTypes.ORANGINA);
        try {
            orderElement.addToDrinks(null);
            fail("Null drink doesn't thrown an exception");
        } catch (IllegalArgumentException e){
            //goood
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
}