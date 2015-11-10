
package ch.epfl.sweng.udle;

import org.junit.Test;

import ch.epfl.sweng.udle.Food.DrinkTypes;

import static junit.framework.Assert.assertEquals;


/**
 * Created by rodri on 30/10/2015.
 */
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
        DrinkTypes orangina = DrinkTypes.ORANGINA;

        assertEquals("Orangina", orangina.toString());
    }
    @Test
    public void oranginaTestPrice(){
        DrinkTypes orangina = DrinkTypes.ORANGINA;

        assertEquals(5.00, orangina.getPrice());
    }
}