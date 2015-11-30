package ch.epfl.sweng.udle;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by rodri on 30/10/2015.
 */
public class MenuTest {


    FoodTypes food = FoodTypes.KEBAB;
    ArrayList<OptionsTypes> options = new ArrayList<>();

    public void addOneToOptions(ArrayList<OptionsTypes> options){
        options.add(OptionsTypes.SALAD);
    }
    public void addTwoToOptions(ArrayList<OptionsTypes> options){
        options.add(OptionsTypes.SALAD);
        options.add(OptionsTypes.TOMATO);
    }

    @Test
    public void correctInit(){
        Menu menu = new Menu();

        assertEquals(menu.getFood(), null);
        assertEquals(menu.getOptions(), options);
    }

    @Test
    public void correctFood(){
        Menu menu = new Menu();

        menu.setFood(food);
        assertEquals(food, menu.getFood());
        assertEquals(menu.getOptions(), options);
    }

    @Test
    public void nullFood(){
        Menu menu = new Menu();

        try {
            menu.setFood(null);
            fail("Set Food to null didn't throw an exception.");
        } catch (IllegalArgumentException e){
            //Good
        }
    }


    @Test
    public void correctAddToOption(){
        Menu menu = new Menu();

        addOneToOptions(options);

        try {
            menu.addToOptions(OptionsTypes.SALAD);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Add to options throw an unexpected excption");
        }
    }

    @Test
    public void correctRemoveToOption(){
        Menu menu = new Menu();

        try {
            menu.addToOptions(OptionsTypes.SALAD);
            menu.removeFromOptions(OptionsTypes.SALAD);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Remove from optionsList throw an unexpected excption");
        }
    }


    @Test
    public void addNullToOption(){
        Menu menu = new Menu();

        try {
            menu.addToOptions(null);
            fail("Add null to optionsList didn't throws an exception");
        } catch (IllegalArgumentException e){
            //Good
        }
    }

    @Test
    public void removeNullToOption(){
        Menu menu = new Menu();

        try {
            menu.removeFromOptions(null);
            fail("Remove null to optionsList didn't throws an exception");
        } catch (IllegalArgumentException e){
            //Good
        }
    }


    @Test
    public void addTwiceToOption(){
        Menu menu = new Menu();
        addOneToOptions(options);

        try {
            menu.addToOptions(OptionsTypes.SALAD);
            menu.addToOptions(OptionsTypes.SALAD);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Add twice the same option to  optionsList throw an unexpected excption");
        }
    }

    @Test
    public void addTwiceRemoveOnceToOption(){
        Menu menu = new Menu();

        try {
            menu.addToOptions(OptionsTypes.SALAD);
            menu.addToOptions(OptionsTypes.SALAD);
            menu.removeFromOptions(OptionsTypes.SALAD);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Add twice the same option and remove it once from optionsList throw an unexpected excption");
        }
    }

    @Test
    public void removeNoExistingFromOptionEmpty(){
        Menu menu = new Menu();

        try {
            menu.removeFromOptions(OptionsTypes.ALGERIENNE);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Unexpected error thrown.");
        }
    }
    @Test
    public void removeNoExistingFromOptionNotEmpty(){
        Menu menu = new Menu();
        menu.addToOptions(OptionsTypes.SALAD);

        addOneToOptions(options);

        try {
            menu.removeFromOptions(OptionsTypes.ALGERIENNE);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Unexpected error thrown.");
        }
    }


    @Test
    public void addTwoToOption(){
        Menu menu = new Menu();
        addTwoToOptions(options);

        try {
            menu.addToOptions(OptionsTypes.SALAD);
            menu.addToOptions(OptionsTypes.TOMATO);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Add two option to optionsList throw an unexpected excption");
        }
    }

    @Test
    public void addTwoRemoveOneToOption(){
        Menu menu = new Menu();
        addOneToOptions(options);

        try {
            menu.addToOptions(OptionsTypes.SALAD);
            menu.addToOptions(OptionsTypes.TOMATO);
            menu.removeFromOptions(OptionsTypes.TOMATO);
            assertEquals(options, menu.getOptions());
        } catch (IllegalArgumentException e){
            fail("Add two option and remove one from optionsList throw an unexpected excption");
        }
    }

}