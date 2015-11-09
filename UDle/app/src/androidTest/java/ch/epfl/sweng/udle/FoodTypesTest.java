package ch.epfl.sweng.udle;

import org.junit.Test;


import ch.epfl.sweng.udle.Food.FoodTypes;

import static junit.framework.Assert.assertEquals;

/**
 * Created by rodri on 30/10/2015.
 */
public class FoodTypesTest {


    @Test
    public void kebabTestToString(){
        FoodTypes kebab = FoodTypes.KEBAB;

        assertEquals("Kebab", kebab.toString());
    }
    @Test
    public void kebabTestPrice(){
        FoodTypes kebab = FoodTypes.KEBAB;

        assertEquals(10.00, kebab.getPrice());
    }
    @Test
    public void kebabTestMaxNbr(){
        FoodTypes kebab = FoodTypes.KEBAB;

        assertEquals(10, kebab.getMaxNbr());
    }




    @Test
    public void burgerTestToString(){
        FoodTypes burger= FoodTypes.BURGER;

        assertEquals("Burger", burger.toString());
    }
    @Test
    public void burgerTestPrice(){
        FoodTypes burger = FoodTypes.BURGER;

        assertEquals(10.00, burger.getPrice());
    }
    @Test
    public void burgerTestMaxNbr(){
        FoodTypes burger = FoodTypes.BURGER;

        assertEquals(10, burger.getMaxNbr());
    }

}