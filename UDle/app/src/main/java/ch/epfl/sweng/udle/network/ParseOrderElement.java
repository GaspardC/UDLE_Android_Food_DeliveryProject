package ch.epfl.sweng.udle.network;

import android.location.Location;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;

/**
 * Created by rodri on 28/11/2015.
 *
 * This class take care of pushing an OrderElement to the server and tranform the data given by the server into a correct OrderElement.
 */

public class ParseOrderElement {

    /**
     * Creates a ParseObject which will go to the table OrderElement from an OrderElement object.
     * The server only accept basic types: So we convert the orderElement into an object wich only contains strings and list of strings.
     * Push this object to server.
     * Return the object.
     */
    public static ParseObject create(OrderElement orderElement) {

        ParseObject parseOrderElement = new ParseObject("OrderElement");

        // Convert the orderList of Menu into a list of strings.
        List<List<String>> orderList = new ArrayList<>();
        for (Menu menu : orderElement.getMenus()) {
            List<String> menuElement = new ArrayList<>();
            menuElement.add(menu.getFood().toString());
            for (OptionsTypes options : menu.getOptions()) {
                menuElement.add(options.toString());
            }
            orderList.add(menuElement);
        }
        parseOrderElement.put("orderList", orderList);

        //Convert the list of DrinkTypes into a list of String.
        List<String> drinks = new ArrayList<>();
        for (DrinkTypes drink : orderElement.getDrinks()) {
            drinks.add(drink.toString());
        }
        parseOrderElement.put("drinks", drinks);

        //Convert the deliveryLocation from Location to ParseGeoPoint.
        Location deliveryLocation = orderElement.getDeliveryLocation();
        ParseGeoPoint dLocation = new ParseGeoPoint(deliveryLocation.getLatitude(), deliveryLocation.getLongitude());
        parseOrderElement.put("deliveryLocation", dLocation);

        //The following variables can be push directly, no need to convert them.
        parseOrderElement.put("deliveryAddress", orderElement.getDeliveryAddress());
        parseOrderElement.put("orderedBy", orderElement.getOrderedUserName());
        parseOrderElement.put("userOrderInformationsId", orderElement.getUserOrderInformationsID());

        return parseOrderElement;
    }


    /**
     * @param parseOrderElement Object retrieve from the server
     * @return An OrderElement object created from the data retrieve by the server as a parseOrderElement object.
     */
    public static OrderElement retrieveOrderElementFromParse(ParseObject parseOrderElement) {
        OrderElement orderElement = new OrderElement();

        //-------------------     MENU      -------------------------------
        List<List<String>> orderList = (List<List<String>>) parseOrderElement.get("orderList");
        for (List<String> menuElement: orderList){
            Menu menu = new Menu();

            for(String menuElementString: menuElement){
                for(FoodTypes foodTypes: FoodTypes.values()){
                    if (menuElementString.equals(foodTypes.toString())){
                        menu.setFood(foodTypes);
                    }
                }
                for(OptionsTypes optionsTypes: OptionsTypes.values()){
                    if (menuElementString.equals(optionsTypes.toString())){
                        menu.addToOptions(optionsTypes);
                    }
                }
            }

            orderElement.addMenu(menu);
        }

        //-------------------     DRINKS      -------------------------------
        List<String> drinksList = (List<String>) parseOrderElement.get("drinks");
        for (String drinksString : drinksList){
            for (DrinkTypes drinkTypes : DrinkTypes.values()){
                if (drinksString.equals(drinkTypes.toString())){
                    orderElement.addToDrinks(drinkTypes);
                }
            }
        }

        //-------------------     LOCATION      -------------------------------
        ParseGeoPoint dLocation = (ParseGeoPoint) parseOrderElement.get("deliveryLocation");
        Location deliveryLocation = new Location("");
        deliveryLocation.setLatitude(dLocation.getLatitude());
        deliveryLocation.setLongitude(dLocation.getLongitude());
        orderElement.setDeliveryLocation(deliveryLocation);


        //-------------------     ADDRESSS      -------------------------------
        String address = (String) parseOrderElement.get("deliveryAddress");
        orderElement.setDeliveryAddress(address);

        //-------------------     ORDERED BY      -------------------------------
        String orderedBy = (String) parseOrderElement.get("orderedBy");
        orderElement.setOrderedUserName(orderedBy);

        //-------------------     USER ORDER INFORMATION ID      -------------------------------
        String userOrderInformationsId = (String) parseOrderElement.get("userOrderInformationsId");
        orderElement.setUserOrderInformationsID(userOrderInformationsId);


        return orderElement;
    }
}

