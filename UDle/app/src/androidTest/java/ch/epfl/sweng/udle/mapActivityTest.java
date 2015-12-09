package ch.epfl.sweng.udle;

import android.app.Instrumentation;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.parse.ParseObject;
import com.parse.ParseUser;


import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.MapActivity;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;
import ch.epfl.sweng.udle.network.ParseOrderElement;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Abdes on 08/12/2015.
 */
public class mapActivityTest extends ActivityInstrumentationTestCase2<MapActivity> {
    private MapActivity myActivity;

    public mapActivityTest() {
        super(MapActivity.class);
    }

    public void setUp() throws Exception {
        ParseUser.logIn("restaurant2", "test");
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        myActivity = getActivity();
    }

    @Test
    public void checkGpsPopUp() throws InterruptedException {
        // If no error and gps disabled, means that the pop up appeared and was dismissed correctly
        checkGps();
    }

    @Test
    public void testAutocompView() throws InterruptedException {
        Thread.sleep(5000);
        checkGps();
        final AutoCompleteTextView autocomp = (AutoCompleteTextView) myActivity.findViewById(R.id.autoCompleteTextView2);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                autocomp.setText("Unit test");
            }
        });
        onView(withId(R.id.autoCompleteTextView2)).check(matches(withText("Unit test")));
    }

    @Test
    public void testMapVisble() throws InterruptedException {
        Thread.sleep(3000);
        checkGps();
        onView(withId(R.id.MenuMap_GoogleMaps)).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonAfterClick() throws InterruptedException {
        Thread.sleep(3000);
        if (checkGps()) {
            try {
                testOpenNextActivity(myActivity, true);
            } catch (Exception e_1) {
                fail("Should start Menu Activity");
            }
        }
    }

    @Test
    public void testRealAddress() throws InterruptedException {
        Thread.sleep(5000);
        checkGps();
        final AutoCompleteTextView autocomp = (AutoCompleteTextView) myActivity.findViewById(R.id.autoCompleteTextView2);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                autocomp.setText(R.string.adressTest);
            }
        });
        Thread.sleep(5000);


        onView(withText("Rue de Bourg, Lausanne, Switzerland")).inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        onView(withText("Rue de Bourg, Lausanne, Switzerland")).inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView())))).perform(click());

        try {
            testOpenNextActivity(myActivity, true);
        } catch (Exception e_1) {
            fail("Should start Menu Activity");
        }


    }

    public void testOpenNextActivity(final MapActivity myActivity, boolean shouldBeTrue) {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        final Button button = (Button) myActivity.findViewById(R.id.MenuMap_ValidatePosition);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });
        //Watch for the timeout
        MainActivity nextActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 10000);
        // next activity is opened and captured.
        if (shouldBeTrue) {
            assertNotNull(nextActivity);
            nextActivity .finish();
        }else
            assertNull(nextActivity);
    }

    private OrderElement getOrderElement(){
        OrderElement orderElement = new OrderElement();
        orderElement.setDeliveryAddress("Address Delivery Test");
        Location location = new Location("");
        location.setLongitude(45.0);
        location.setLatitude(50.44);
        orderElement.setDeliveryLocation(location);
        orderElement.setUserOrderInformationsID("user order informations ID test");
        orderElement.setOrderedUserName("UserName Test");
        orderElement.setOrderList(getMenuList());
        return orderElement;
    }

    private ArrayList<Menu> getMenuList(){
        ArrayList<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menuList.add(menu);
        return menuList;
    }

    private Boolean checkGps(){
        try{
            onView(withText(R.string.mapActivityNoGps)).check(matches(isDisplayed()));
            onView(withText("No")).perform((click()));
            Thread.sleep(3000);
            try{
                onView(withText(R.string.mapActivityNoGps)).check(matches(isDisplayed()));
                fail("Should closed");
            }catch (Exception e_1){
                return false;
            }
        }catch (Exception e){
            return true;
        }
        return true;
    }

}
