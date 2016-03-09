/*
package ch.epfl.sweng.udle;

import android.location.Location;

import com.parse.ParseObject;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.network.ParseOrderElement;
import static junit.framework.Assert.assertEquals;
*/
/**
 * Created by rodri on 30/11/2015.
 *//*

public class ParseOrderElementTest {


    @Test
    public void testCreateAndRetrieve(){
        ParseObject parseOrderElement = ParseOrderElement.create(getOrderElement());
        OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(parseOrderElement);

        assertEquals("Address Delivery Test" , orderElement.getDeliveryAddress());

        Location location = orderElement.getDeliveryLocation();
        assertEquals(45.0, location.getLongitude());
        assertEquals(50.44, location.getLatitude());

        assertEquals("user order informations ID test", orderElement.getUserOrderInformationsID());

        assertEquals("UserName Test", orderElement.getOrderedUserName());

        ArrayList<DrinkTypes> drinks = orderElement.getDrinks();
        assertEquals(4, drinks.size());
        assertEquals(true, drinks.contains(DrinkTypes.BEER));
        drinks.remove(DrinkTypes.BEER);
        assertEquals(true, drinks.contains(DrinkTypes.COCA));
        drinks.remove(DrinkTypes.COCA);
        assertEquals(true, drinks.contains(DrinkTypes.COCA));
        drinks.remove(DrinkTypes.COCA);
        assertEquals(true, drinks.contains(DrinkTypes.COCA));
        drinks.remove(DrinkTypes.COCA);
        assertEquals(true, drinks.isEmpty());

        ArrayList<Menu> menu = orderElement.getMenus();
        assertEquals(2, menu.size());
        Menu menu1 = menu.get(0);
        assertEquals(FoodTypes.KEBAB.toString(),menu1.getFood().toString());
        ArrayList<OptionsTypes> options1 = menu1.getOptions();
        assertEquals(1, options1.size());
        assertEquals(OptionsTypes.ALGERIENNE.toString(), options1.get(0).toString());
        Menu menu2 = menu.get(1);
        assertEquals(FoodTypes.BURGER.toString(),menu2.getFood().toString());
        ArrayList<OptionsTypes> options2 = menu2.getOptions();
        assertEquals(2, options2.size());
        assertEquals(OptionsTypes.TOMATO.toString(), options2.get(0).toString());
        assertEquals(OptionsTypes.SALAD.toString(), options2.get(1).toString());

    }


    private OrderElement getOrderElement(){
        OrderElement orderElement = new OrderElement();

        orderElement.setDeliveryAddress("Address Delivery Test");

        Location location = new Location("");
        location.setLongitude(45.0);
        location.setLatitude(50.44);
        orderElement.setDeliveryLocation(location);

        orderElement.setUserOrderInformationsID("user order informations ID test");

        orderElement.setOrderedUserName("UserName Test");

        orderElement.setOrderList(getMenuList());

        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.COCA);

        return orderElement;
    }

    private ArrayList<Menu> getMenuList(){
        ArrayList<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        menuList.add(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.BURGER);
        menu1.addToOptions(OptionsTypes.TOMATO);
        menu1.addToOptions(OptionsTypes.SALAD);
        menuList.add(menu1);
        return menuList;
    }
}
*/
