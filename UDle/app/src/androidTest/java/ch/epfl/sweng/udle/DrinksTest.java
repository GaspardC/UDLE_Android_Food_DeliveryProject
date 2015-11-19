package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by rodri on 19/11/2015.
 */
public class DrinksTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public DrinksTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        OrderElement orderElement = new OrderElement();
        Orders.setActiveOrder(orderElement);
    }

    public void testDrinksDontSetTo0(){
        //Test a previous bug: If drinks were added, and we go to another activity (e.g. MenuFragment) and come back to drinks, it will not display the real number fo drinks, but 0.
        getActivity();
        onView(withText("Drinks")).perform(click());
        onView(withId(R.id.cocaPlus)).perform((click()));
        onView(withId(R.id.cocaNbr)).check(matches(withText("1")));
        onView(withText("Menu")).perform(click());
        onView(withText("Drinks")).perform(click());
        onView(withId(R.id.cocaNbr)).check(matches(withText("1")));
    }
}
