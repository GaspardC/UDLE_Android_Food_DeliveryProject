package ch.epfl.sweng.udle;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AdapterView;
import android.widget.ListView;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.RecapActivity;
import ch.epfl.sweng.udle.network.DataManager;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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

        orderElement.setOrderedUserName("Test userName");
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

    public void addMenus_2(){
        OrderElement orderElement = new OrderElement();
        Menu KebabAlone = new Menu();
        KebabAlone.setFood(FoodTypes.KEBAB);
        orderElement.addMenu(KebabAlone);
        orderElement.addMenu(KebabAlone);
        Menu BurgerAlone = new Menu();
        BurgerAlone.setFood(FoodTypes.BURGER);
        orderElement.addMenu(BurgerAlone);

        Orders.setActiveOrder(orderElement);
    }

    public void addMenus_3(){
        OrderElement orderElement = new OrderElement();

        Menu BurgerEverything = new Menu();
        BurgerEverything.setFood(FoodTypes.BURGER);
        for (OptionsTypes foodTypes : OptionsTypes.values()){
            BurgerEverything.addToOptions(foodTypes);
        };

        Menu KebabEverything = new Menu();
        KebabEverything.setFood(FoodTypes.KEBAB);
        for (OptionsTypes foodTypes : OptionsTypes.values()){
            KebabEverything.addToOptions(foodTypes);
        };
        orderElement.addMenu(BurgerEverything);
        orderElement.addMenu(BurgerEverything);
        orderElement.addMenu(KebabEverything);
        Orders.setActiveOrder(orderElement);
    }


    @Test
    public void testUserName(){
        addDrinks();
        getActivity();
        onView(withId(R.id.RecapActivity_deliveryName)).check(matches(withText("Test userName")));
    }


    @Test
    public void testMultipleDrinks(){
        addDrinks();
        getActivity();
        onView(withText("4 Beer")).check(matches(isDisplayed()));
        onView(withText("2 Coca")).check(matches(isDisplayed()));
        onView(withText("1 Water")).check(matches(isDisplayed()));
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
        onView(withText("2 Kebab")).check(matches(isDisplayed()));
        onView(withText("1 Burger")).check(matches(isDisplayed()));
        onView(withText("2 Burger")).check(matches(isDisplayed()));
/*        onView(withText("4 Burger")).perform(ViewActions.scrollTo());
        onView(withText("4 Burger")).check(matches(isDisplayed()));

        onView(withText("Options: Salad ; Tomato ; Oignon ; ")).check(matches(isDisplayed()));

        String everything = "Options: ";
        for (OptionsTypes foodTypes : OptionsTypes.values()){
            everything += foodTypes.toString()+" ; ";
        }
        onView(withText(everything)).check(matches(isDisplayed()));
*/
    }

    @Test
    public void testDeleteAllOnMenusWithoutOptions(){
        addMenus_2();
        final RecapActivity activity = getActivity();
        onView(withText("2 Kebab")).check(matches(isDisplayed()));
        onView(withText("1 Burger")).check(matches(isDisplayed()));

        final ListView mList = (ListView) activity.findViewById(R.id.RecapActivity_recapListView);
        final int mActivePosition = 0;
        mList.performItemClick(
                mList.getChildAt(mActivePosition),
                mActivePosition,
                mList.getAdapter().getItemId(mActivePosition));

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.deleteElement(mActivePosition, true);
            }
        });
        try{
            onView(withText("2 Kebab")).check(matches(isDisplayed()));
            fail("Should be deleted");
        }catch (Exception e){
            // Works
        }
    }

    @Test
    public void testDeleteAllOnMenusWithOptions(){
        addMenus_3();
        final RecapActivity activity = getActivity();
        onView(withText("2 Burger")).check(matches(isDisplayed()));
        onView(withText("1 Kebab")).check(matches(isDisplayed()));

        final int mActivePosition = 0;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.deleteElement(mActivePosition, true);
            }
        });
        try{
            onView(withText("2 Burger")).check(matches(isDisplayed()));
            fail("Should be deleted");
        }catch (Exception e){
            // Works
        }
    }

    @Test
    public void testDeleteOneOnMenusWithoutOptions(){
        addMenus_2();
        final RecapActivity activity = getActivity();
        onView(withText("2 Kebab")).check(matches(isDisplayed()));
        onView(withText("1 Burger")).check(matches(isDisplayed()));

        final ListView mList = (ListView) activity.findViewById(R.id.RecapActivity_recapListView);
        final int mActivePosition = 0;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.deleteElement(mActivePosition, false);
            }
        });
        try{
            onView(withText("2 Kebab")).check(matches(isDisplayed()));
            fail("Should be deleted");
        }catch (Exception e){
            onView(withText("1 Kebab")).check(matches(isDisplayed()));
            onView(withText("1 Burger")).check(matches(isDisplayed()));
            final int mActivePosition_2 = 1;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.deleteElement(mActivePosition_2, false);
                }
            });
            try{
                onView(withText("1 Burger")).check(matches(isDisplayed()));
                fail("Should be deleted");
            }catch (Exception e_1){
                // Works
            }
        }
    }

    @Test
    public void testDeleteOneOnMenusWithOptions(){
        addMenus_3();
        final RecapActivity activity = getActivity();
        onView(withText("2 Burger")).check(matches(isDisplayed()));
        onView(withText("1 Kebab")).check(matches(isDisplayed()));

        final int mActivePosition = 0;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.deleteElement(mActivePosition, false);
            }
        });
        try{
            onView(withText("2 Burger")).check(matches(isDisplayed()));
            fail("Should be deleted");
        }catch (Exception e){
            onView(withText("1 Burger")).check(matches(isDisplayed()));
            onView(withText("1 Kebab")).check(matches(isDisplayed()));
            final int mActivePosition_2 = 1;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.deleteElement(mActivePosition_2, false);
                }
            });
            try{
                onView(withText("1 Kebab")).check(matches(isDisplayed()));
                fail("Should be deleted");
            }catch (Exception e_1){
                // Works
            }
        }
    }

    @Test
    public void testDeleteAllOnDrinks(){
        addDrinks();
        final RecapActivity activity = getActivity();
        onView(withText("4 Beer")).check(matches(isDisplayed()));
        onView(withText("2 Coca")).check(matches(isDisplayed()));
        onView(withText("1 Water")).check(matches(isDisplayed()));

        final int mActivePosition = 2;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.deleteElement(mActivePosition, true);
            }
        });
        try{
            onView(withText("4 Beer")).check(matches(isDisplayed()));
            fail("Should be deleted");
        }catch (Exception e){
            // Works
        }
    }

    @Test
    public void testDeleteOneOnDrinks(){
        addDrinks();
        final RecapActivity activity = getActivity();
        onView(withText("4 Beer")).check(matches(isDisplayed()));
        onView(withText("2 Coca")).check(matches(isDisplayed()));
        onView(withText("1 Water")).check(matches(isDisplayed()));

        final int mActivePosition = 2;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.deleteElement(mActivePosition, false);
            }
        });
        try{
            onView(withText("4 Beer")).check(matches(isDisplayed()));
            fail("Should be deleted");
        }catch (Exception e){
            onView(withText("3 Beer")).check(matches(isDisplayed()));
        }
    }

}
