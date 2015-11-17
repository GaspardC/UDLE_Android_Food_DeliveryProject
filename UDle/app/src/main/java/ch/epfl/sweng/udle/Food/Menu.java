package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodri on 23/10/2015.
 */
public class Menu {

    private FoodTypes food;
    private ArrayList<OptionsTypes> options;

    public Menu() {
        this.food = null;
        this.options = new ArrayList<>();
    }


    public FoodTypes getFood() {
        return food;
    }

    public void setFood(FoodTypes food) {
        if (food == null) {
            throw new IllegalArgumentException("Try to set the foodType of a menu to null.");
        }
        this.food = food;
    }


    public ArrayList<OptionsTypes> getOptions() {
        return options;
    }

    public void addToOptions(OptionsTypes option) {
        if (option == null) {
            throw new IllegalArgumentException("Try to add a null option to the optionsType List.");
        }
        if (!this.options.contains(option)) {
            this.options.add(option);
        }
    }

    public void removeFromOptions(OptionsTypes option) {
        if (option == null) {
            throw new IllegalArgumentException("Try to remove a null option to the optionsType List.");
        }
        if (this.options.contains(option)) {
            this.options.remove(option);
        }
    }

    public static void displayInRecap(List<HashMap<String, String>> list){
        for(Menu menu : Orders.getActiveOrder().getOrder()){
            String food = "1 " + menu.getFood().toString();
            String price = String.format("%.2f", menu.getFood().getPrice());
            price = price + Orders.getMoneyDevise();

            String option = "";
            for( OptionsTypes opt : menu.getOptions()){
                option = option + opt.toString() + " ; " ;
            }

            HashMap<String, String> element = new HashMap<>();
            element.put("elem", food);
            element.put("price", price);
            element.put("options", option);

            list.add(element);
        }
    }
}