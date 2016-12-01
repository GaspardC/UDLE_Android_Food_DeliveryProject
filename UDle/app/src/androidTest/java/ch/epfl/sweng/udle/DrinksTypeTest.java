/*

package ch.epfl.sweng.udle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;

import static junit.framework.Assert.assertEquals;


*/
/**
 * Created by rodri on 30/10/2015.
 *//*

public class DrinksTypeTest {


    @Test
    public void waterTestToString(){
        DrinkTypes water = DrinkTypes.WATER;

        assertEquals("Water", water.toString());
    }
    @Test
    public void waterTestPrice(){
        DrinkTypes water = DrinkTypes.WATER;

        assertEquals(5.00, water.getPrice());
    }



    @Test
    public void cocaTestToString(){
        DrinkTypes coca = DrinkTypes.COCA;

        assertEquals("Coca", coca.toString());
    }
    @Test
    public void cocaTestPrice(){
        DrinkTypes coca= DrinkTypes.COCA;

        assertEquals(5.00, coca.getPrice());
    }




    @Test
    public void beetTestToString() {
        DrinkTypes beer = DrinkTypes.BEER;

        assertEquals("Beer", beer.toString());
    }
    @Test
    public void beerTestPrice(){
        DrinkTypes beer = DrinkTypes.BEER;

        assertEquals(5.00, beer.getPrice());
    }



    @Test
    public void oranginaTestToString(){
        DrinkTypes orangina = DrinkTypes.FANTA;

        assertEquals("Orangina", orangina.toString());
    }
    @Test
    public void oranginaTestPrice(){
        DrinkTypes orangina = DrinkTypes.FANTA;

        assertEquals(5.00, orangina.getPrice());
    }


    @Test
    public void testDisplayInRecap(){
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        OrderElement orderElement = new OrderElement();
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.FANTA);
        orderElement.addToDrinks(DrinkTypes.FANTA);
        orderElement.addToDrinks(DrinkTypes.FANTA);
        orderElement.addToDrinks(DrinkTypes.WATER);
        Orders.setActiveOrder(orderElement);

        DrinkTypes.displayInRecap(list);

        //We add Beer, Orangina and Water, so size must be 3
        assertEquals(3, list.size());

        HashMap<String, String > beer = list.get(1);
        assertEquals("5 Beer", beer.get("elem"));
        String priceBeer = "25.00" + Orders.getMoneyDevise();
        assertEquals(priceBeer, beer.get("price"));

        HashMap<String, String > orangina = list.get(2);
        assertEquals("3 Orangina", orangina.get("elem"));
        String priceOrangina = "15.00" + Orders.getMoneyDevise();
        assertEquals(priceOrangina, orangina.get("price"));

        HashMap<String, String > water = list.get(0);
        assertEquals("1 Water", water.get("elem"));
        String priceWater = "5.00" + Orders.getMoneyDevise();
        assertEquals(priceWater, water.get("price"));


    }
}*/
