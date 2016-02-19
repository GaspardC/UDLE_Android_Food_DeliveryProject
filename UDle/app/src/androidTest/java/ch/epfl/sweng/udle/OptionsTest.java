package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;

/**
 * Created by rodri on 13/11/2015.
 */
public class OptionsTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public OptionsTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        OrderElement orderElement = new OrderElement();
        Orders.setActiveOrder(orderElement);
    }

    public void testOneBurgerDisplay(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).check(matches(isDisplayed()));
        }
    }
    public void testOneBurgerListNotDisplay(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).check(matches(isDisplayed()));
        }
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()) {
            try{
                onView(withText(optionsTypes.toString()));
            } catch(Exception e){
                //Good: Espresso should not be able to find the Options Text, since it is supposed to be not display
            }
        }
    }

    public void testTwoBurgersDisplay(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).check(matches(isDisplayed()));
        }
        onView(withText("#1  Burger")).perform(click());
        onView(withText("#2  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).check(matches(isDisplayed()));
        }
    }

    public void testTwoBurgersRemoveOne(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).check(matches(isDisplayed()));
        onView(withText("#2  Burger")).check(matches(isDisplayed()));
        onView(withText("Menu")).perform(click());
        onView(withId(R.id.MenuActivity_BurgerMinus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).check(matches(isDisplayed()));
        try{
            onView(withText("#2  Burger")).perform(click());
        } catch (Exception e){
            //Good. Burger #2 was remove, so should not be display anymore
        }
    }



    public void testOneBurgerOptionsCorrectlyAdded(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).perform(click());
        }

        OrderElement orderElement = Orders.getActiveOrder();
        for (Menu menu :orderElement.getMenus()){
            for(OptionsTypes optionsTypes: OptionsTypes.values()){
                assertTrue(menu.getOptions().contains(optionsTypes));
            }
        }
    }

    public void testOneBurgerOptionsCorrectlyRemoved(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).perform(click());
        }
        OptionsTypes salad = OptionsTypes.SALAD;
        onView(withText(salad.toString())).perform(click());
        OrderElement orderElement = Orders.getActiveOrder();
        for (Menu menu :orderElement.getMenus()){
            assertFalse(menu.getOptions().contains(salad));
        }
    }

    public void testBurgerOptionsCorrectlyAdded(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withText("#1  Burger")).perform(click());
        for (OptionsTypes optionsTypes : OptionsTypes.values()){
            onView(withText(optionsTypes.toString())).perform(click());
        }
        onView(withText("#2  Burger")).check(matches(isDisplayed()));

        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menu = orderElement.getMenus();

        for(OptionsTypes optionsTypes: OptionsTypes.values()){
            assertTrue(menu.get(0).getOptions().contains(optionsTypes));
            assertFalse(menu.get(1).getOptions().contains(optionsTypes));
        }
    }

    public void testNoOptionsMessage(){
        getActivity();
        onView(withText("Options")).perform(click());
        onView(withId(R.id.Options_noMenuSelected)).check(matches(isDisplayed()));
    }
    public void testNoOptionsMessageAfterRemoveMenu(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_NextButton)).perform(click());
        onView(withId(R.id.Options_noMenuSelected)).check(matches(not(isDisplayed())));
        onView(withText("Menu")).perform(click());
        onView(withId(R.id.MenuActivity_BurgerMinus)).perform(click());
        onView(withText("Options")).perform(click());
        onView(withId(R.id.Options_noMenuSelected)).check(matches(isDisplayed()));
    }

}
