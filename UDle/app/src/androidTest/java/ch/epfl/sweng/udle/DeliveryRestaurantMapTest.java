package ch.epfl.sweng.udle;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.ActivityInstrumentationTestCase2;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.DeliveryRestaurantMapActivity;
import ch.epfl.sweng.udle.network.DataManager;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by rodri on 06/11/2015.
 */
public class DeliveryRestaurantMapTest  extends ActivityInstrumentationTestCase2<DeliveryRestaurantMapActivity> {

    private DeliveryRestaurantMapActivity mActivity;

    public DeliveryRestaurantMapTest() {
        super(DeliveryRestaurantMapActivity.class);
    }

    @Before
    public void setUp() throws Exception {

        ParseUser.logIn("test deliveryRestaurantMap", "test");

        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        ArrayList<OrderElement> orderElements = new ArrayList<>();
        Orders.setActiveOrder(getOrderElement());
        Orders.activeOrderToCurrentOrder(Orders.getActiveOrder());
        orderElements.add(getOrderElement());
        mActivity.setWaitingOrdersForTesting(orderElements);
        Thread.sleep(10000);
    }



    @Test
    public void testMarkerClickable() throws UiObjectNotFoundException, InterruptedException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Address test, 1022, Switwerland"));
        marker.click();
    }


    @Test
    public void testListInvisble() throws InterruptedException {
        onView(withId(R.id.listOrderRestaurantMap)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
    @Test
    public void testMapVisble() throws InterruptedException {
        Thread.sleep(5000);
        onView(withId(R.id.DeliveryMap_GoogleMaps)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityAfterClick(){
        onView(withId(R.id.button_list_mode)).perform(click());
        onView(withId(R.id.DeliveryMap_GoogleMaps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.listOrderRestaurantMap)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.button_list_mode)).perform(click());
        onView(withId(R.id.DeliveryMap_GoogleMaps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.listOrderRestaurantMap)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testButtonTextAfterClick() throws InterruptedException {
        onView(withId((R.id.button_list_mode))).check(matches(withText("Switch to List mode")));
        onView(withId(R.id.button_list_mode)).perform(click());
        onView(withId((R.id.button_list_mode))).check(matches(withText("Switch to Map Mode")));
        onView(withId(R.id.button_list_mode)).perform(click());
    }


    @Test
    public void testIfNoOrders() throws InterruptedException {
        mActivity.resetCurrentOrder();
        mActivity.resetWaitingOrders();
        onView(withId(R.id.button_list_mode)).perform(click());
        onData(anything())
                .inAdapterView(withContentDescription("listOrderRestaurantMap"))
                .atPosition(0)
                .check(matches(hasDescendant(withText("No orders for now "))));

        onData(anything())
                .inAdapterView(withContentDescription("listOrderRestaurantMap"))
                .atPosition(0)
                .check(matches(hasDescendant(withText("Wait a moment please"))));

    }

    public OrderElement getOrderElement (){
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        menu1.addToOptions(OptionsTypes.KETCHUP);
        menu1.addToOptions(OptionsTypes.SALAD);
        OrderElement orderElement1 = new OrderElement();
        orderElement1.addMenu(menu1);
        orderElement1.addToDrinks(DrinkTypes.BEER);
        Location location1 = new Location("");
        location1.setLatitude(46.519);
        location1.setLongitude(6.566);
        orderElement1.setDeliveryLocation(location1);
        orderElement1.setDeliveryAddress("Address test, 1022, Switwerland");
        orderElement1.setOrderedUserName("User Name 1");
        return orderElement1;
    }

}