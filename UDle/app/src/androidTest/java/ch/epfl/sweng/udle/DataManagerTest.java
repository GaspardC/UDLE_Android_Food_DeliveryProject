package ch.epfl.sweng.udle;

import android.location.Location;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.network.DataManager;
import ch.epfl.sweng.udle.network.ParseUserOrderInformations;

import static junit.framework.Assert.assertEquals;

/**
 * Created by skalli93 on 11/10/15.
 */
public class DataManagerTest {


    @Test
    public void testGetUsername() throws ParseException {
        ParseUser.logIn("UserTest", "test");
        assertEquals("UserTest", DataManager.getUserName());
    }

    @Test
    public void testUserLocation() throws ParseException {
        ParseUser.logIn("UserTest", "test");
        Location location = new Location("");
        location.setLatitude(46.056);
        location.setLongitude(50.22222);

        DataManager.setUserLocation(location);
        Location rLocation = DataManager.getUserLocation();

        assertEquals(rLocation.getLongitude(), 50.22222);
        assertEquals(rLocation.getLatitude(), 46.056);
    }

    @Test
    public void testExpectedTime1() throws ParseException, InterruptedException {
        ParseUser.logIn("UserTest", "test");
        OrderElement orderElement = getOrderElement();
        Orders.setActiveOrder(orderElement);
        DataManager.createNewParseUserOrderInformations();
        String objectId = Orders.getActiveOrder().getUserOrderInformationsID();

        Thread.sleep(3000);
        String eta = DataManager.getExpectedTime(objectId);
        assertEquals("-1", eta);

        //Delete entries created for test in the database.
        ParseUserOrderInformations parseUserOrderInformations = DataManager.getParseUserObjectWithId(objectId);
        parseUserOrderInformations.getOrder().delete();
        deleteParseUserOrderInformation(objectId);
    }

    @Test
    public void testIsStatusWaiting() throws ParseException, InterruptedException {
        ParseUser.logIn("UserTest", "test");
        OrderElement orderElement = getOrderElement();
        Orders.setActiveOrder(orderElement);
        DataManager.createNewParseUserOrderInformations();

        String objectId = Orders.getActiveOrder().getUserOrderInformationsID();
        Thread.sleep(3000);
        boolean isStatusWaiting = DataManager.isStatusWaiting(objectId);
        assertEquals(true, isStatusWaiting);

        //Delete entries created for test in the database.
        ParseUserOrderInformations parseUserOrderInformations = DataManager.getParseUserObjectWithId(objectId);
        parseUserOrderInformations.getOrder().delete();
        deleteParseUserOrderInformation(objectId);
    }

    @Test
    public void testIsStatusWaitingTrue() throws ParseException {
        ParseUser.logIn("user test alone", "test");
        boolean restaurantNear = DataManager.getRestaurantsNearTheUser();
        assertEquals(true, restaurantNear);
    }
    @Test
    public void testIsStatusWaitingFalse() throws ParseException {
        ParseUser.logIn("test alone", "test");
        boolean restaurantNear = DataManager.getRestaurantsNearTheUser();
        assertEquals(false, restaurantNear);
    }


    @Test
    public void testWaitingOrdersNull() throws ParseException {
        ParseUser.logIn("user test alone", "test");
        ArrayList<OrderElement> expectedOrders = new ArrayList<>();
        assertEquals(expectedOrders, DataManager.getWaitingOrdersForAClient());
    }
    @Test
    public void testWaitingOrders() throws ParseException {
        ParseUser.logIn("UserTest", "test");
        ArrayList<OrderElement> orders = DataManager.getWaitingOrdersForAClient();
        assertEquals(1, orders.size());
        OrderElement order = orders.get(0);
        OrderElement orderElement = getOrderElementUserTest();

        assertEquals(orderElement.getOrder().get(0).getFood().toString(), order.getOrder().get(0).getFood().toString());
        assertEquals(orderElement.getDrinks().get(0).toString(), order.getDrinks().get(0).toString());
        assertEquals(0, order.getOrder().get(0).getOptions().size());
    }

    @Test
    public void testWaitingOrdersRestaurant() throws ParseException {
        ParseUser.logIn("resto test full", "test");
        ArrayList<OrderElement> orders = DataManager.getWaitingOrdersForARestaurantOwner();
        assertEquals(1, orders.size());
        OrderElement order = orders.get(0);
        OrderElement orderElement = getOrderElementUserTest();

        assertEquals(orderElement.getOrder().get(0).getFood().toString(), order.getOrder().get(0).getFood().toString());
        assertEquals(orderElement.getDrinks().get(0).toString(), order.getDrinks().get(0).toString());
        assertEquals(0, order.getOrder().get(0).getOptions().size());
    }
    @Test
    public void testCurrentOrdersRestaurant() throws ParseException {
        ParseUser.logIn("resto test full", "test");
        ArrayList<OrderElement> orders = DataManager.getCurrentOrdersForARestaurantOwner();
        assertEquals(1, orders.size());
        OrderElement order = orders.get(0);
        OrderElement orderElement = getOrderElementUserTest();

        assertEquals(orderElement.getOrder().get(0).getFood().toString(), order.getOrder().get(0).getFood().toString());
        assertEquals(orderElement.getDrinks().get(0).toString(), order.getDrinks().get(0).toString());
        assertEquals(0, order.getOrder().get(0).getOptions().size());
    }
    @Test
    public void testIsARestaurantTrue() throws ParseException {
        ParseUser.logIn("resto test full", "test");
        boolean isRestaurant = DataManager.isARestaurant();
        assertEquals(true, isRestaurant);
    }

    @Test
    public void testIsARestaurantFalse() throws ParseException {
        ParseUser.logIn("UserTest", "test");
        boolean isRestaurant = DataManager.isARestaurant();
        assertEquals(false, isRestaurant);
    }




    @Test
    public void testEnRouteOrdersNull() throws ParseException {
        ParseUser.logIn("user test alone", "test");
        ArrayList<OrderElement> expectedOrders = new ArrayList<>();
        assertEquals(expectedOrders, DataManager.getEnRouteOrdersForAClient());
    }
    @Test
    public void testEnRouteOrders() throws ParseException {
        ParseUser.logIn("UserTest", "test");
        ArrayList<OrderElement> orders = DataManager.getEnRouteOrdersForAClient();
        assertEquals(1, orders.size());
        OrderElement order = orders.get(0);
        OrderElement orderElement = getOrderElementUserTest();

        assertEquals(orderElement.getOrder().get(0).getFood().toString(), order.getOrder().get(0).getFood().toString());
        assertEquals(orderElement.getDrinks().get(0).toString(), order.getDrinks().get(0).toString());
        assertEquals(0, order.getOrder().get(0).getOptions().size());
    }





    /* HELPERS METHOD */

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
        orderElement1.setDeliveryAddress("Address for the deliver 1, 1002 SwEng TestDataManager");
        orderElement1.setOrderedUserName("User Name 1");
        return orderElement1;
    }

    public OrderElement getOrderElementUserTest (){
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.KEBAB);
        OrderElement orderElement1 = new OrderElement();
        orderElement1.addMenu(menu1);
        orderElement1.addToDrinks(DrinkTypes.BEER);
        Location location1 = new Location("");
        location1.setLatitude(0.00);
        location1.setLongitude(0.00);
        orderElement1.setDeliveryLocation(location1);
        return orderElement1;
    }

    private void deleteParseUserOrderInformation(String objectId){
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





}
