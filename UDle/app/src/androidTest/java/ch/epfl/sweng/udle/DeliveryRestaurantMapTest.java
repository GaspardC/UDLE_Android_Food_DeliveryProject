package ch.epfl.sweng.udle;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.activities.DeliveryRestaurantMapActivity;
import ch.epfl.sweng.udle.network.DataManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by rodri on 06/11/2015.
 */
public class DeliveryRestaurantMapTest {

    @Rule
    public ActivityTestRule<DeliveryRestaurantMapActivity> mActivityRule = new ActivityTestRule<>(
            DeliveryRestaurantMapActivity.class);

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