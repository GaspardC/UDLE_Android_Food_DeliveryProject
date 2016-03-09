/*
package ch.epfl.sweng.udle;

import android.app.Instrumentation;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.parse.ParseUser;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.PaymentActivity;
import ch.epfl.sweng.udle.activities.WaitingActivity;
import ch.epfl.sweng.udle.network.DataManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

*/
/**
 * Created by Abdes on 07/12/2015.
 *//*

public class PaymentTest extends ActivityInstrumentationTestCase2<PaymentActivity> {

    public PaymentTest(){
        super(PaymentActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        ParseUser.logIn("restaurant3", "test");
        OrderElement orderElement = getOrderElement();
        Orders.setActiveOrder(orderElement);
        super.setUp();
        ViewActions.closeSoftKeyboard();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }
    @Override
    protected void tearDown (){

    }
    @Test
    public void testAllInfoInvalid() {
        PaymentActivity myActivity = getActivity();
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.payment_cardNumber)).perform(typeText("AAA"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_expDate)).perform(typeText("BBB"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_securityNumber)).perform(typeText("CCC"), ViewActions.closeSoftKeyboard());

        try{
            testOpenNextActivity(myActivity, false);
        }catch(Exception e){
            fail("Should not start Waiting Activity");
        }
    }

    @Test
    public void testCardNumbInvalid() {
        PaymentActivity myActivity = getActivity();
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.payment_cardNumber)).perform(typeText("AAA"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_expDate)).perform(typeText("BBBB"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_securityNumber)).perform(typeText("CCCC"), ViewActions.closeSoftKeyboard());

        try{
            testOpenNextActivity(myActivity, false);
        }catch(Exception e){
            fail("Should not start Waiting Activity");
        }
    }

    @Test
    public void testExpDateInvalid() {
        PaymentActivity myActivity = getActivity();
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.payment_cardNumber)).perform(typeText("AAAA"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_expDate)).perform(typeText("BBB"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_securityNumber)).perform(typeText("CCCC"), ViewActions.closeSoftKeyboard());

        try{
            testOpenNextActivity(myActivity, false);
        }catch(Exception e){
            fail("Should not start Waiting Activity");
        }
    }

    @Test
    public void testSecuNumberInvalid() {
        PaymentActivity myActivity = getActivity();
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.payment_cardNumber)).perform(typeText("AAAA"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_expDate)).perform(typeText("BBBB"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_securityNumber)).perform(typeText("CCC"), ViewActions.closeSoftKeyboard());

        try{
            testOpenNextActivity(myActivity, false);
        }catch(Exception e){
            fail("Should not start Waiting Activity");
        }
    }


    @Test
    public void testAllInfoValid() {
        PaymentActivity myActivity = getActivity();
        ViewActions.closeSoftKeyboard();
        onView(withId(R.id.payment_cardNumber)).perform(typeText("AAAA"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_expDate)).perform(typeText("BBBB"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.payment_securityNumber)).perform(typeText("CCCC"), ViewActions.closeSoftKeyboard());

        try{
            testOpenNextActivity(myActivity, true);
        }catch(Exception e){
            fail("Should start Waiting Activity");
        }
    }
    public void testOpenNextActivity(final PaymentActivity myActivity, boolean shouldBeTrue) {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(WaitingActivity.class.getName(), null, false);
        final Button button = (Button) myActivity.findViewById(R.id.button_payment_confirm);
        onView(withId((R.id.button_payment_confirm))).perform(click());
        //Watch for the timeout
        WaitingActivity nextActivity = (WaitingActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 2000);
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



}
*/
