package ch.epfl.sweng.udle.Food;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodri on 23/10/2015.
 */
public enum DrinkTypes {

    WATER("Water", 5.00),
    COCA("Coca", 5.00),
    BEER("Beer", 5.00),
    ORANGINA("Orangina", 5.00);

    private String name = "";
    private double price = -1;

    DrinkTypes(String name, double price){
        this.name = name;
        this.price = price;
    }

    public Double getPrice(){
        return price;
    }

    public String toString(){
        return name;
    }


    public static void displayInRecap(List<HashMap<String, String>> list){
        for(DrinkTypes drinkType : DrinkTypes.values()){
            int nbaOccurrences = Collections.frequency(Orders.getActiveOrder().getDrinks(), drinkType);

            if (nbaOccurrences != 0){
                String drink = String.valueOf(nbaOccurrences) +"x " + drinkType.toString();
                String price = String.format("%.2f", drinkType.getPrice()*nbaOccurrences);
                price = price + Orders.getMoneyDevise();

                HashMap<String, String> element = new HashMap<>();
                element.put("elem", drink);
                element.put("price", price);
                element.put("options", "");

                list.add(element);
            }
        }
    }
}
