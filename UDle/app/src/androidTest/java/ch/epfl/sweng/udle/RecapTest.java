package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.RecapActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by rodri on 14/11/2015.
 */
public class RecapTest extends ActivityInstrumentationTestCase2<RecapActivity> {

    public RecapTest() {
        super(RecapActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    public void addDrinks(){
        OrderElement orderElement = new OrderElement();

        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.WATER);

        Orders.setActiveOrder(orderElement);
    }

    public void addMenus(){
        OrderElement orderElement = new OrderElement();

        Menu KebabAlone = new Menu();
        KebabAlone.setFood(FoodTypes.KEBAB);
        Menu BurgerAlone = new Menu();
        BurgerAlone.setFood(FoodTypes.BURGER);

        Menu BurgerEverything = new Menu();
        BurgerEverything.setFood(FoodTypes.BURGER);
        for (OptionsTypes foodTypes : OptionsTypes.values()){
            BurgerEverything.addToOptions(foodTypes);
        }

        Menu BurgerSTO = new Menu();
        BurgerSTO.setFood(FoodTypes.BURGER);
        BurgerSTO.addToOptions(OptionsTypes.SALAD);
        BurgerSTO.addToOptions(OptionsTypes.TOMATO);
        BurgerSTO.addToOptions(OptionsTypes.OIGNON);

        orderElement.addMenu(KebabAlone);
        orderElement.addMenu(KebabAlone);
        orderElement.addMenu(BurgerAlone);
        orderElement.addMenu(BurgerEverything);
        orderElement.addMenu(BurgerEverything);
        orderElement.addMenu(BurgerSTO);
        orderElement.addMenu(BurgerSTO);
        orderElement.addMenu(BurgerSTO);
        orderElement.addMenu(BurgerSTO);

        Orders.setActiveOrder(orderElement);
    }


    @Test
    public void testMultipleDrinks(){
        addDrinks();
        getActivity();
        onView(withText("4x Beer")).check(matches(isDisplayed()));
        onView(withText("2x Coca")).check(matches(isDisplayed()));
        onView(withText("1x Water")).check(matches(isDisplayed()));
        try{
            onView(withText("Orangina"));
        } catch (Exception e){
            //Good: The drink "Orangina" is not added to the drinks for this order. So it should not appear on the activity.
        }
    }

    @Test
    public void testMultipleMenus(){
        addMenus();
        getActivity();
        onView(withText("2x Kebab")).check(matches(isDisplayed()));
        onView(withText("1x Burger")).check(matches(isDisplayed()));
        onView(withText("2x Burger")).check(matches(isDisplayed()));
        onView(withText("4x Burger")).check(matches(isDisplayed()));

        onView(withText("Options:  Salad ; Tomato ; Oignon ; ")).check(matches(isDisplayed()));

        String everything = "Options:  ";
        for (OptionsTypes foodTypes : OptionsTypes.values()){
            everything += foodTypes.toString()+" ; ";
        }
        onView(withText(everything)).check(matches(isDisplayed()));

    }

}
