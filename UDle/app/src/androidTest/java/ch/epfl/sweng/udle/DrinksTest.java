/*
package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.DeliveryRestaurantMapActivity;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

*/
/**
 * Created by rodri on 19/11/2015.
 *//*

public class DrinksTest extends ActivityInstrumentationTestCase2<MainActivity> {


    private MainActivity mActivity;

    public DrinksTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp(){
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        OrderElement orderElement = new OrderElement();
        Orders.setActiveOrder(orderElement);
        mActivity = getActivity();
        onView(withText("Drinks")).perform(click());


    }



*/
/*    @Test
    public void testDrinksDontSetTo0(){
        //Test a previous bug: If drinks were added, and we go to another activity (e.g. MenuFragment) and come back to drinks, it will not display the real number fo drinks, but 0.
        onView(withId(R.id.cocaPlus)).perform((click()));
        onView(withId(R.id.cocaNbr)).check(matches(withText("1")));
        onView(withText("Menu")).perform(click());
        onView(withText("Drinks")).perform(click());
        onView(withId(R.id.cocaNbr)).check(matches(withText("1")));
    }




    @Test
    public void testDrinkPlus() {
        //Test that the orangInit function works correctly
        onView(withId(R.id.beerPlus)).perform((click()));
        onView(withId(R.id.cocaPlus)).perform((click()));
        onView(withId(R.id.waterPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withText("Drinks")).perform(click());
        onView(withId(R.id.cocaNbr)).check(matches(withText("1")));
        onView(withId(R.id.beerNbr)).check(matches(withText("1")));
        onView(withId(R.id.waterNbr)).check(matches(withText("1")));
        onView(withId(R.id.orangNbr)).check(matches(withText("1")));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangNbr)).check(matches(withText("2")));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangNbr)).check(matches(withText("3")));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangNbr)).check(matches(withText("4")));

        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangNbr)).check(matches(withText("10")));

    }

    @Test
    public void testDrinksMinus(){

        onView(withId(R.id.cocaPlus)).perform((click()));
        onView(withId(R.id.cocaMinus)).perform((click()));
        onView(withId(R.id.cocaNbr)).check(matches(withText("0")));

        onView(withId(R.id.beerPlus)).perform((click()));
        onView(withId(R.id.beerPlus)).perform((click()));

        onView(withId(R.id.beerMinus)).perform((click()));
        onView(withId(R.id.beerNbr)).check(matches(withText("1")));

        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangMinus)).perform((click()));
        onView(withId(R.id.orangMinus)).perform((click()));

        onView(withId(R.id.orangNbr)).check(matches(withText("1")));

        onView(withId(R.id.waterMinus)).perform((click()));
        onView(withId(R.id.waterMinus)).perform((click()));
        onView(withId(R.id.waterPlus)).perform((click()));

        onView(withId(R.id.waterNbr)).check(matches(withText("1")));

    }
    @Test
    public void testDrinksDontGoUnder0() {
        //Test that if we click - the drinks number don't go under 0

        onView(withId(R.id.cocaMinus)).perform((click()));
        onView(withId(R.id.cocaMinus)).perform((click()));
        onView(withId(R.id.cocaMinus)).perform((click()));
        onView(withId(R.id.cocaMinus)).perform((click()));

        onView(withId(R.id.cocaNbr)).check(matches(withText("0")));

        onView(withId(R.id.beerMinus)).perform((click()));
        onView(withId(R.id.beerNbr)).check(matches(withText("0")));
        onView(withId(R.id.orangMinus)).perform((click()));
        onView(withId(R.id.orangNbr)).check(matches(withText("0")));
        onView(withId(R.id.waterMinus)).perform((click()));
        onView(withId(R.id.waterNbr)).check(matches(withText("0")));
    }


    @Test
    public void testDrinksComputePrice(){

        onView(withId(R.id.cocaPlus)).perform((click()));
        onView(withId(R.id.cocaTotal)).check(matches(withText(DrinkTypes.COCA.getPrice() + Orders.getMoneyDevise())));
        onView(withId(R.id.cocaPlus)).perform((click()));
        onView(withId(R.id.cocaTotal)).check(matches(withText(2 * DrinkTypes.COCA.getPrice() + Orders.getMoneyDevise())));

        onView(withId(R.id.beerPlus)).perform((click()));
        onView(withId(R.id.beerTotal)).check(matches(withText(DrinkTypes.BEER.getPrice() + Orders.getMoneyDevise())));

        onView(withId(R.id.waterPlus)).perform((click()));
        onView(withId(R.id.waterTotal)).check(matches(withText(DrinkTypes.WATER.getPrice() + Orders.getMoneyDevise())));

        onView(withId(R.id.orangPlus)).perform((click()));
        onView(withId(R.id.orangTotal)).check(matches(withText(DrinkTypes.ORANGINA.getPrice()+ Orders.getMoneyDevise())));
    }*//*


}
*/
