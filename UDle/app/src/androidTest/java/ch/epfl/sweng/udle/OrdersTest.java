package ch.epfl.sweng.udle;

import org.junit.Test;

import java.util.ArrayList;


import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
/**
 * Created by rodri on 02/11/2015.
 */
public class OrdersTest {


    public OrderElement getOrderElem(){
        OrderElement orderElement = new OrderElement();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu1);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.WATER);
        return orderElement;
    }

    @Test
    public void correctActiveOrder(){
        OrderElement orderElement = new OrderElement();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu1);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.WATER);

        Orders orders = new Orders();
        orders.setActiveOrder(orderElement);

        OrderElement orderElem = getOrderElem();
        ArrayList<Menu> menus = orderElem.getOrder();
        ArrayList<DrinkTypes> drinks = orderElem.getDrinks();
        for (int i=0 ; i < menus.size() ; i++){
            assertEquals(menus.get(i).getFood().getMaxNbr(), orders.getActiveOrder().getOrder().get(i).getFood().getMaxNbr());
            assertEquals(menus.get(i).getFood().getPrice(), orders.getActiveOrder().getOrder().get(i).getFood().getPrice());
            assertEquals(menus.get(i).getFood().toString(), orders.getActiveOrder().getOrder().get(i).getFood().toString());
            assertEquals(menus.get(i).getOptions(), orders.getActiveOrder().getOrder().get(i).getOptions());
        }
        for (int i=0; i < drinks.size() ; i++){
            assertEquals(drinks.get(i).toString(), orderElement.getDrinks().get(i).toString());
            assertEquals(drinks.get(i).getPrice(), orderElement.getDrinks().get(i).getPrice());
        }
    }
    @Test
    public void nullActiveOrder() {
        Orders orders = new Orders();
        orders.setActiveOrder(null);
        assertEquals(null, orders.getActiveOrder());
    }

    @Test
    public void activeToCurrent(){
        OrderElement orderElement = new OrderElement();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu1);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.WATER);

        Orders orders = new Orders();
        orders.setActiveOrder(orderElement);

        try{
            orders.activeOrderToCurrentOrder(orderElement);
            for (OrderElement order : orders.getCurrentOrders()){
                assertEquals(orderElement, order);
            }
            assertEquals(null, orders.getActiveOrder());
        } catch (IllegalArgumentException e){
            fail();
        }
    }
    @Test
    public void notSameActiveToCurrent(){
        OrderElement orderElement = new OrderElement();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        orderElement.addToDrinks(DrinkTypes.WATER);

        OrderElement orderElement1 = new OrderElement();
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.BURGER);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement1.addMenu(menu1);
        orderElement1.addToDrinks(DrinkTypes.WATER);

        Orders orders = new Orders();
        orders.setActiveOrder(orderElement);

        try{
            orders.activeOrderToCurrentOrder(orderElement1);
            fail("Active order is not the same but doesn't thrown an error");
        } catch (IllegalArgumentException e){
            //good
        }
    }

    @Test
    public void currentToFinish(){
        OrderElement orderElement = new OrderElement();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu1);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.WATER);

        Orders orders = new Orders();
        orders.setActiveOrder(orderElement);
        orders.activeOrderToCurrentOrder(orderElement);
        try{
            Orders.currentOrderFinished(orderElement);
            for (OrderElement order : orders.getOldOrders()){
                assertEquals(orderElement, order);
            }
            assertEquals(null, orders.getActiveOrder());
            assertEquals(0, orders.getCurrentOrders().size());
        } catch (IllegalArgumentException e){
            fail();
        }
    }
    @Test
    public void notInCurrentToFinish(){
        OrderElement orderElement = new OrderElement();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement.addMenu(menu);
        orderElement.addToDrinks(DrinkTypes.BEER);

        OrderElement orderElement1 = new OrderElement();
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.ALGERIENNE);
        orderElement1.addMenu(menu1);
        orderElement1.addToDrinks(DrinkTypes.WATER);

        Orders orders = new Orders();
        orders.setActiveOrder(orderElement);

        try{
            orders.activeOrderToCurrentOrder(orderElement1);
            fail("Current order not in list doesn't thrown an error");
        } catch (IllegalArgumentException e){
            //good
        }
    }
}