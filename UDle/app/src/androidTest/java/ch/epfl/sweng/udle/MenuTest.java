package ch.epfl.sweng.udle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by rodri on 30/10/2015.
 */
public class MenuTest {


    FoodTypes food = FoodTypes.KEBAB;
    ArrayList<OptionsTypes> options = new ArrayList<>();

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

    @Test
    public void testEqualsTrue(){
        Menu menu = new Menu();
        menu.addToOptions(OptionsTypes.OIGNON);
        menu.addToOptions(OptionsTypes.MUSTARD);
        menu.addToOptions(OptionsTypes.MAYO);
        menu.setFood(FoodTypes.KEBAB);

        Menu menu2 = new Menu();
        menu2.addToOptions(OptionsTypes.OIGNON);
        menu2.addToOptions(OptionsTypes.MUSTARD);
        menu2.addToOptions(OptionsTypes.MAYO);
        menu2.setFood(FoodTypes.KEBAB);

        assertEquals(true, menu.equals(menu2));
    }

    @Test
    public void testEqualsFalse(){
        Menu menu = new Menu();
        menu.addToOptions(OptionsTypes.OIGNON);
        menu.addToOptions(OptionsTypes.MUSTARD);
        menu.setFood(FoodTypes.KEBAB);

        Menu menu2 = new Menu();
        menu2.addToOptions(OptionsTypes.OIGNON);
        menu2.addToOptions(OptionsTypes.MUSTARD);
        menu2.addToOptions(OptionsTypes.MAYO);
        menu2.setFood(FoodTypes.KEBAB);

        assertEquals(false, menu.equals(menu2));
    }
    @Test
    public void testEqualsEmptyArgument(){
        Menu menu = new Menu();
        Menu menu2 = new Menu();

       try{
           menu.equals(menu2);
           fail("Empty menu as argument does not throw an exception.");
       } catch (IllegalArgumentException e){
           //Good
       }
    }
    @Test
    public void testEqualsEmpty(){
        Menu menu = new Menu();
        Menu menu2 = new Menu();
        menu2.setFood(FoodTypes.KEBAB);

        try{
            menu.equals(menu2);
            fail("Empty menu does not throw an exception.");
        } catch (IllegalStateException e){
            //Good
        }
    }
    @Test
    public void testEqualNull(){
        Menu menu = new Menu();
        menu.addToOptions(OptionsTypes.OIGNON);
        menu.addToOptions(OptionsTypes.MUSTARD);
        menu.setFood(FoodTypes.KEBAB);

        try{
            menu.equals(null);
            fail("Null menu to compare does not throw an error");
        } catch (IllegalArgumentException e){
            //Good
        }
    }

    @Test
    public void testDisplayInRecap(){
        Menu menu1 = new Menu();
        menu1.addToOptions(OptionsTypes.OIGNON);
        menu1.addToOptions(OptionsTypes.MUSTARD);
        menu1.setFood(FoodTypes.KEBAB);

        Menu menu2a = new Menu();
        menu2a.addToOptions(OptionsTypes.OIGNON);
        menu2a.addToOptions(OptionsTypes.SALAD);
        menu2a.addToOptions(OptionsTypes.KETCHUP);
        menu2a.addToOptions(OptionsTypes.ALGERIENNE);
        menu2a.addToOptions(OptionsTypes.TOMATO);
        menu2a.setFood(FoodTypes.BURGER);
        Menu menu2b = new Menu();
        menu2b.addToOptions(OptionsTypes.OIGNON);
        menu2b.addToOptions(OptionsTypes.SALAD);
        menu2b.addToOptions(OptionsTypes.KETCHUP);
        menu2b.addToOptions(OptionsTypes.ALGERIENNE);
        menu2b.addToOptions(OptionsTypes.TOMATO);
        menu2b.setFood(FoodTypes.BURGER);

        Menu menu3 = new Menu();
        menu3.setFood(FoodTypes.KEBAB);

        OrderElement orderElement = new OrderElement();
        orderElement.addMenu(menu1);
        orderElement.addMenu(menu2a);
        orderElement.addMenu(menu2b);
        orderElement.addMenu(menu3);

        Orders.setActiveOrder(orderElement);


        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list, "No options.", "Options: ");

        assertEquals(3, list.size());

        HashMap<String, String> hashMapMenu1 = list.get(0);
        assertEquals("1 Kebab", hashMapMenu1.get("elem"));
        String price1 = "10.00"+Orders.getMoneyDevise();
        assertEquals(price1, hashMapMenu1.get("price"));
        String options1 = "Options: Oignon ; Mustard ; ";
        assertEquals(options1, hashMapMenu1.get("options"));

        HashMap<String, String> hashMapMenu2 = list.get(1);
        assertEquals("2 Burger", hashMapMenu2.get("elem"));
        String price2 = "20.00"+Orders.getMoneyDevise();
        assertEquals(price2, hashMapMenu2.get("price"));
        String options2 = "Options: Oignon ; Salad ; Ketchup ; Sauce Algerienne ; Tomato ; ";
        assertEquals(options2, hashMapMenu2.get("options"));

        HashMap<String, String> hashMapMenu3 = list.get(2);
        assertEquals("1 Kebab", hashMapMenu3.get("elem"));
        String price3 = "10.00"+Orders.getMoneyDevise();
        assertEquals(price3, hashMapMenu3.get("price"));
        String options3 = "No options.";
        assertEquals(options3, hashMapMenu3.get("options"));
    }



    public void addOneToOptions(ArrayList<OptionsTypes> options){
        options.add(OptionsTypes.SALAD);
    }
    public void addTwoToOptions(ArrayList<OptionsTypes> options){
        options.add(OptionsTypes.SALAD);
        options.add(OptionsTypes.TOMATO);
    }

}