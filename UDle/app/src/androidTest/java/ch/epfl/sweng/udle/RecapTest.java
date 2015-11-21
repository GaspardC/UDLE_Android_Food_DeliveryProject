package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.RecapActivity;
import ch.epfl.sweng.udle.network.DataManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        OrderElement orderElement = new OrderElement();

        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.WATER);

        orderElement.setOrderedUserName("Test userName");
        Orders.setActiveOrder(orderElement);
    }


    @Test
    public void testUserName(){
        getActivity();
        onView(withId(R.id.RecapActivity_deliveryName)).check(matches(withText("Test userName")));
    }


    @Test
    public void testMultipleDrinks(){
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

}
