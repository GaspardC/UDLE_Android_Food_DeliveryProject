package ch.epfl.sweng.udle;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.activities.DeliveryRestaurantMapActivity;
import ch.epfl.sweng.udle.network.DataManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.content.res.TypedArrayUtils.getResourceId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

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
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }



    @Test
    public void testListInvisble(){

            onView(withId(R.id.listOrderRestaurantMap)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

    }
    @Test
    public void testMapVisble(){
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
    public void testButtonTextAfterClick(){

        onView(withId((R.id.button_list_mode))).check(matches(withText("Switch to List mode")));
        onView(withId(R.id.button_list_mode)).perform(click());
        onView(withId((R.id.button_list_mode))).check(matches(withText("Switch to Map Mode")));
        onView(withId(R.id.button_list_mode)).perform(click());
        onView(withId((R.id.button_list_mode))).check(matches(withText("Switch to List Mode")));
    }

    @Test
    public void testButtonGoToNextActivityWhenClickOnAnOrder(){

        onView(withId(R.id.button_list_mode)).perform(click());
        final ArrayList<OrderElement> waitingOrders = mActivity.getWaitingOrders(new ArrayList<OrderElement>());
        String adress = waitingOrders.get(0).getDeliveryAddress();
        for(int i=0;i<waitingOrders.size();i++){
            onData(anything()).inAdapterView(withContentDescription("listOrderRestaurantMap")).atPosition(i).perform(click());
            onView(withId(R.id.DeliverCommandDetail_recapListView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            pressBack();
        }

    }





//    @Rule
//    public ActivityTestRule<DeliveryRestaurantMapActivity> mActivityRule = new ActivityTestRule<>(
//            DeliveryRestaurantMapActivity.class);

 /* Wait for method   DataManager.getPendingOrdersForARestaurantOwner() on master branch.
    @Test
    public void testQuizClientGetterSetter() throws UiObjectNotFoundException {
        DeliveryRestaurantMapActivity activity = mActivityRule.getActivity();

        ArrayList<OrderElement> waitingOrders = DataManager.getPendingOrdersForARestaurantOwner();
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        for(OrderElement order : waitingOrders){
            UiObject marker = device.findObject(new UiSelector().descriptionContains(order.getDeliveryAddress()));
            marker.click();
        }
    }*/
}