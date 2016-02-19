package ch.epfl.sweng.udle;

import android.location.Location;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.network.DataManager;
import ch.epfl.sweng.udle.network.OrderStatus;
import ch.epfl.sweng.udle.network.ParseOrderElement;
import ch.epfl.sweng.udle.network.ParseUserOrderInformations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by rodri on 05/12/2015.
 */
public class ParseUserOrderInformationsTest {

    @Test
    public void testGetSetUser() throws ParseException, InterruptedException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        ParseUser.logIn("UserTest", "test");

        ParseUser myUser = DataManager.getUser();
        parseUserOrderInformations.setUser(myUser);
        Thread.sleep(2000);
        ParseUser user = parseUserOrderInformations.getUser();

        //If the objectId are the same, we can be sure it is exactly the same users.
        assertEquals(myUser.getObjectId(), user.getObjectId());

        parseUserOrderInformations.delete();
    }
    @Test
    public void testGetUserNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.getUser();
            fail("User not set doesn't throw an error.");
        } catch (InternalError internalError){
            //Good
        }
    }
    @Test
    public void testSetUserNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setUser(null);
            fail("Set user to null doesn't throw an error.");
        } catch (Exception e){
            //Good
        }
    }







    @Test
    public void testDeliveryGuyNumber() throws InterruptedException, ParseException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        parseUserOrderInformations.setDeliveryGuyNumber("000 333 784");
        Thread.sleep(2000);
        String numberRetrieved = parseUserOrderInformations.getDeliveryGuyNumber();

        assertEquals("000 333 784", numberRetrieved);

        parseUserOrderInformations.delete();
    }
    @Test
    public void testGetDeliveryGuyNumberNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.getDeliveryGuyNumber();
            fail("Delivery guy number not set doesn't throw an error.");
        } catch (InternalError internalError){
            //Good
        }
    }
    @Test
    public void testSetDeliveryGuyNumberNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setDeliveryGuyNumber(null);
            fail("Set delivery guy number to null doesn't throw an error.");
        } catch (Exception e){
            //Good
        }
    }




    @Test
    public void testDeliveryRestaurant() throws InterruptedException, ParseException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();

        parseUserOrderInformations.setParseDeliveringRestaurantName("restaurant Test");
        Thread.sleep(2000);
        String restaurant = parseUserOrderInformations.getParseDeliveringRestaurant();

        assertEquals("restaurant Test", restaurant);

        parseUserOrderInformations.delete();
    }
    @Test
    public void testGetDeliveryRestaurantrNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.getParseDeliveringRestaurant();
            fail("Delivery restaurant not set doesn't throw an error.");
        } catch (InternalError internalError){
            //Good
        }
    }
    @Test
    public void testSetDeliveryGuyRestaurantNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setParseDeliveringRestaurantName(null);
            fail("Set delivery restaurant to null doesn't throw an error.");
        } catch (Exception e){
            //Good
        }
    }
    @Test
    public void testSetDeliveryGuyRestaurantEmpty(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setParseDeliveringRestaurantName("");
            fail("Set delivery restaurant to empty doesn't throw an error.");
        } catch (Exception e){
            //Good
        }
    }





    @Test
    public void testExpectedTime() throws InterruptedException, ParseException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        parseUserOrderInformations.setExpectedTime(49);


        Date d = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm, dd/MM");
        int time = (49* 60 * 1000);
        Date deliveryDate = new Date(d.getTime() + (time));
        String date = fmt.format(deliveryDate);

        Thread.sleep(2000);
        String expectedTime = parseUserOrderInformations.getExpectedTime();

        assertEquals(date, expectedTime);

        parseUserOrderInformations.delete();
    }
    @Test
    public void testGetExpectedTimeNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.getExpectedTime();
            fail("Expected time not set doesn't throw an error.");
        } catch (InternalError internalError){
            //Good
        }
    }



    @Test
    public void testOrerStatusWaiting() throws InterruptedException, ParseException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();

        parseUserOrderInformations.setOrderStatus(OrderStatus.WAITING.toString());
        Thread.sleep(2000);
        String orderStatus = parseUserOrderInformations.getOrderStatus();
        assertEquals("Waiting", orderStatus);

        parseUserOrderInformations.delete();
    }
    @Test
    public void testOrerStatusEnRoute() throws InterruptedException, ParseException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();

        parseUserOrderInformations.setOrderStatus(OrderStatus.ENROUTE.toString());
        Thread.sleep(2000);
        String orderStatus = parseUserOrderInformations.getOrderStatus();
        assertEquals("EnRoute", orderStatus);

        parseUserOrderInformations.delete();
    }
    @Test
    public void testOrerStatusFinish() throws InterruptedException, ParseException {
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();

        parseUserOrderInformations.setOrderStatus(OrderStatus.DELIVERED.toString());
        Thread.sleep(2000);
        String orderStatus = parseUserOrderInformations.getOrderStatus();
        assertEquals("Delivered", orderStatus);

        parseUserOrderInformations.delete();
    }
    @Test
    public void testGetOrderStatusNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.getOrderStatus();
            fail("Order status not set doesn't throw an error.");
        } catch (InternalError internalError){
            //Good
        }
    }
    @Test
    public void testSetOrderStatusNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setOrderStatus(null);
            fail("Set order status to null doesn't throw an error.");
        } catch (Exception e){
            //Good
        }
    }
    @Test
    public void testSetOrderStatusEmpty(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setOrderStatus("");
            fail("Set order Status to empty doesn't throw an error.");
        } catch (Exception e){
            //Good
        }
    }


    @Test
    public void testGetOrderNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.getOrder();
            fail("Order not set doesn't throw an error.");
        } catch (InternalError internalError){
            //Good
        }
    }
    @Test
    public void testSetOrderNull(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();
        try{
            parseUserOrderInformations.setOrder(null);
            fail("Order set to null doesn't throw an error.");
        } catch (IllegalArgumentException e){
            //Good
        }
    }


    @Test
    public void testOrder(){
        ParseUserOrderInformations parseUserOrderInformations = new ParseUserOrderInformations();

        ParseObject parseOrderElement = ParseOrderElement.create(getOrderElement());

        parseUserOrderInformations.setOrder(parseOrderElement);
        ParseObject retrieveOrder = parseUserOrderInformations.getOrder();
        OrderElement orderElement = ParseOrderElement.retrieveOrderElementFromParse(retrieveOrder);

        Location location = orderElement.getDeliveryLocation();
        assertEquals(45.0, location.getLongitude());
        assertEquals(50.44, location.getLatitude());

        assertEquals("user order informations ID test", orderElement.getUserOrderInformationsID());

        assertEquals("UserName Test", orderElement.getOrderedUserName());

        ArrayList<DrinkTypes> drinks = orderElement.getDrinks();
        assertEquals(4, drinks.size());
        assertEquals(true, drinks.contains(DrinkTypes.BEER));
        drinks.remove(DrinkTypes.BEER);
        assertEquals(true, drinks.contains(DrinkTypes.COCA));
        drinks.remove(DrinkTypes.COCA);
        assertEquals(true, drinks.contains(DrinkTypes.COCA));
        drinks.remove(DrinkTypes.COCA);
        assertEquals(true, drinks.contains(DrinkTypes.COCA));
        drinks.remove(DrinkTypes.COCA);
        assertEquals(true, drinks.isEmpty());

        ArrayList<Menu> menu = orderElement.getOrder();
        assertEquals(2, menu.size());
        Menu menu1 = menu.get(0);
        assertEquals(FoodTypes.KEBAB.toString(),menu1.getFood().toString());
        ArrayList<OptionsTypes> options1 = menu1.getOptions();
        assertEquals(1, options1.size());
        assertEquals(OptionsTypes.ALGERIENNE.toString(), options1.get(0).toString());
        Menu menu2 = menu.get(1);
        assertEquals(FoodTypes.BURGER.toString(),menu2.getFood().toString());
        ArrayList<OptionsTypes> options2 = menu2.getOptions();
        assertEquals(2, options2.size());
        assertEquals(OptionsTypes.TOMATO.toString(), options2.get(0).toString());
        assertEquals(OptionsTypes.SALAD.toString(), options2.get(1).toString());

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

        orderElement.addToDrinks(DrinkTypes.BEER);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.COCA);
        orderElement.addToDrinks(DrinkTypes.COCA);

        return orderElement;
    }

    private ArrayList<Menu> getMenuList(){
        ArrayList<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setFood(FoodTypes.KEBAB);
        menu.addToOptions(OptionsTypes.ALGERIENNE);
        menuList.add(menu);
        Menu menu1 = new Menu();
        menu1.setFood(FoodTypes.BURGER);
        menu1.addToOptions(OptionsTypes.TOMATO);
        menu1.addToOptions(OptionsTypes.SALAD);
        menuList.add(menu1);
        return menuList;
    }
}
