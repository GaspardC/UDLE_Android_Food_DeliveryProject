package ch.epfl.sweng.udle;

import android.content.Intent;
import android.location.Location;
import android.support.test.rule.ActivityTestRule;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.activities.DeliverCommandDetailActivity;
import ch.epfl.sweng.udle.network.DataManager;
import ch.epfl.sweng.udle.network.ParseUserOrderInformations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.is;

import static org.hamcrest.Matchers.not;

/**
 * Created by rodri on 06/11/2015.
 */
public class DeliverCommandDetailTest{

    public ActivityTestRule<DeliverCommandDetailActivity> mActivityRule =
            new ActivityTestRule<>(DeliverCommandDetailActivity.class, false, false);
    private String moneyDevise = Orders.getMoneyDevise();
    public OrderElement orderElement = getOrderElement();

    @Test
    public void deliveryAddressTest(){
        setUpIntentAndActivityForNewOrders();
        onView(withId(R.id.DeliverCommandDetail_deliveryAddress)).check(matches(withText("Address for the deliver 1, 1002 SwEng")));
    }
    @Test
    public void deliveryAddressTestEnRouteOrder(){
        setUpIntentAndActivityForEnRouteOrders();
        onView(withId(R.id.DeliverCommandDetail_deliveryAddress)).check(matches(withText("Address for the deliver 1, 1002 SwEng")));
    }

    @Test
    public void deliveryNameTest(){
        setUpIntentAndActivityForNewOrders();
        onView(withId(R.id.DeliverCommandDetail_deliveryName)).check(matches(withText("User Name 1")));
    }
    @Test
    public void deliveryNameTestEnRouteOrder(){
        setUpIntentAndActivityForEnRouteOrders();
        onView(withId(R.id.DeliverCommandDetail_deliveryName)).check(matches(withText("User Name 1")));
    }


    @Test
    public void totalTest(){
        setUpIntentAndActivityForNewOrders();
        String total = String.format("%.2f", orderElement.getTotalCost()) + moneyDevise;
        onView(withId(R.id.DeliverCommandDetail_totalCost)).check(matches(withText(total)));
    }
    @Test
    public void totalTestEnRouteOrder(){
        setUpIntentAndActivityForEnRouteOrders();
        String total = String.format("%.2f", orderElement.getTotalCost()) + moneyDevise;
        onView(withId(R.id.DeliverCommandDetail_totalCost)).check(matches(withText(total)));
    }


    @Test
    public void clickWithoutExpectedTime() {
        setUpIntentAndActivityForNewOrders();
        onView(withId(R.id.DeliverCommandDetail_acceptCommand)).check(matches(withText(R.string.acceptCommand)));
        onView(withId(R.id.DeliverCommandDetail_acceptCommand)).perform(click());
        onView(withText(R.string.expectedTimeNotValid)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
    @Test
    public void clickWithExpectedTimeBig() {
        setUpIntentAndActivityForNewOrders();
        onView(withId(R.id.DeliverCommandDetail_expectedTime)).perform(typeText("150"));
        onView(withId(R.id.DeliverCommandDetail_acceptCommand)).check(matches(withText(R.string.acceptCommand)));
        onView(withId(R.id.DeliverCommandDetail_acceptCommand)).perform(click());
        onView(withText(R.string.expectedTimeTooBig)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

/*    @Test
    public void clickWithExpectedTime() throws ParseException {
        String objectID = setUpIntentAndActivityForNewOrdersServer();
        onView(withId(R.id.DeliverCommandDetail_expectedTime)).perform(typeText("15"));
        onView(withId(R.id.DeliverCommandDetail_acceptCommand)).perform(click());
        onView(withId(R.id.DeliveryMap_GoogleMaps)).check(matches(isDisplayed()));
        deleteParseUserOrderInformation(objectID);
    }
*/


    @Test
    public void clickToFinishOrder() throws ParseException, InterruptedException {
        String objectID = setUpIntentAndActivityForEnRouteOrdersServer();
        onView(withId(R.id.DeliverCommandDetail_commandDelivered)).check(matches(withText(R.string.commandDelivered)));
        onView(withId(R.id.DeliverCommandDetail_commandDelivered)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.DeliveryMap_GoogleMaps)).check(matches(isDisplayed()));
        deleteParseUserOrderInformation(objectID);
    }

    @Test
    public void clickToAcceptOrderConfirmButtonNotShown(){
        setUpIntentAndActivityForNewOrders();
        onView(withId(R.id.DeliverCommandDetail_commandDelivered)).check(matches(not(isDisplayed())));
    }
    @Test
    public void clickToFinishOrderAcceptButtonNotShown(){
        setUpIntentAndActivityForEnRouteOrders();
        onView(withId(R.id.DeliverCommandDetail_acceptCommand)).check(matches(not(isDisplayed())));
    }



    public void setUpIntentAndActivityForNewOrders() {
        Intent i = new Intent();
        i.putExtra("isCurrent", false);
        Orders.setActiveOrder(orderElement);
        mActivityRule.launchActivity(i);
    }

    public void setUpIntentAndActivityForEnRouteOrders() {
        Intent i = new Intent();
        i.putExtra("isCurrent", true);
        Orders.setActiveOrder(orderElement);
        Orders.activeOrderToCurrentOrder(orderElement);

        Orders.setActiveOrder(orderElement); //Selected the second time (Status: 'En route'
        mActivityRule.launchActivity(i);
    }

    private String setUpIntentAndActivityForNewOrdersServer(){
        Intent i = new Intent();
        i.putExtra("isCurrent", false);

        OrderElement orderElement = getOrderElement();
        Orders.setActiveOrder(orderElement);
        DataManager.createNewParseUserOrderInformations();
        String objectId = Orders.getActiveOrder().getUserOrderInformationsID();


        Orders.setActiveOrder(orderElement);
        mActivityRule.launchActivity(i);

        return objectId;
    }
    private String setUpIntentAndActivityForEnRouteOrdersServer(){
        Intent i = new Intent();
        i.putExtra("isCurrent", true);

        OrderElement orderElement = getOrderElement();
        Orders.setActiveOrder(orderElement);

        DataManager.createNewParseUserOrderInformations();
        String objectId = Orders.getActiveOrder().getUserOrderInformationsID();

        DataManager.deliveryEnRoute(objectId, 55);
        Orders.activeOrderToCurrentOrder(orderElement);

        Orders.setActiveOrder(orderElement);
        mActivityRule.launchActivity(i);

        return objectId;
    }
    private void deleteParseUserOrderInformation(String objectId) throws ParseException {

        ParseUserOrderInformations parseUserOrderInformations = DataManager.getParseUserObjectWithId(objectId);
        parseUserOrderInformations.getOrder().delete();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseUserOrderInformations");
        query.whereEqualTo("objectId", objectId);
        try {
            List<ParseObject> OrderList = query.find();

            for (ParseObject userOrder : OrderList) {
                ParseUserOrderInformations parseUserOrder = (ParseUserOrderInformations) userOrder;
                parseUserOrder.delete();
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
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
        orderElement1.setDeliveryAddress("Address for the deliver 1, 1002 SwEng");
        orderElement1.setOrderedUserName("User Name 1");
        return orderElement1;
    }
}