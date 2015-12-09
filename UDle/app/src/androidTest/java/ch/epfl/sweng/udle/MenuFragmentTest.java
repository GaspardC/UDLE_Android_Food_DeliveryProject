package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by rodri on 08/12/2015.
 */
public class MenuFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MenuFragmentTest(){
        super(MainActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        OrderElement orderElement = new OrderElement();
        Orders.setActiveOrder(orderElement);
    }

    @Test
    public void testInit(){
        getActivity();
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("0")));
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("0")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("0.0 CHF")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("0.0 CHF")));
    }

    @Test
    public void testAdd1Kebab(){
        getActivity();
        onView(withId(R.id.MenuActivity_KebabPlus)).perform(click());
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("1")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("10.0 CHF")));
    }
    @Test
    public void testAdd1Burger(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("1")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("10.0 CHF")));
    }
    @Test
    public void testAdd1BurgerNoKebab(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("1")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("10.0 CHF")));
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("0")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("0.0 CHF")));
    }

    @Test
    public void testAdd1KebabAndRemove(){
        getActivity();
        onView(withId(R.id.MenuActivity_KebabPlus)).perform(click());
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("1")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("10.0 CHF")));
        onView(withId(R.id.MenuActivity_KebabMinus)).perform(click());
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("0")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("0.0 CHF")));
    }
    @Test
    public void testAdd1BurgerAndRemove(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerPlus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("1")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("10.0 CHF")));
        onView(withId(R.id.MenuActivity_BurgerMinus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("0")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("0.0 CHF")));
    }

    @Test
    public void testMaxKebab(){
        getActivity();
        int max = FoodTypes.KEBAB.getMaxNbr();
        for( int i=0 ; i < max ; i++){
            onView(withId(R.id.MenuActivity_KebabPlus)).perform(click());
        }
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("10")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("100.0 CHF")));

        onView(withId(R.id.MenuActivity_KebabPlus)).perform(click());
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("10")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("100.0 CHF")));
    }

    @Test
    public void testRemoveBurger(){
        getActivity();
        onView(withId(R.id.MenuActivity_BurgerMinus)).perform(click());
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("0")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("0.0 CHF")));
    }

    @Test
    public void testAlreadyDoneMenu() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        OrderElement orderElement = new OrderElement();
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        orderElement.addMenu(menu1);
        Menu menu2 = new Menu();
        menu2.setFood(FoodTypes.KEBAB);
        orderElement.addMenu(menu2);
        Menu menu3 = new Menu();
        menu3.setFood(FoodTypes.BURGER);
        orderElement.addMenu(menu3);
        Orders.setActiveOrder(orderElement);
        getActivity();
        onView(withId(R.id.MenuActivity_KebabNbr)).check(matches(withText("2")));
        onView(withId(R.id.MenuActivity_BurgerNbr)).check(matches(withText("1")));
        onView(withId(R.id.MenuActivity_KebabTotalMoney)).check(matches(withText("20.0 CHF")));
        onView(withId(R.id.MenuActivity_BurgerTotalMoney)).check(matches(withText("10.0 CHF")));
    }

}
