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

    public boolean equals(Menu menu) {
        int equalityCheckNbr = 0;

        if (this.food.toString().equals(menu.food.toString())){
            for (OptionsTypes options : this.options){
                for (OptionsTypes optionsToCompare : menu.getOptions()){
                    if (options.toString() == optionsToCompare.toString()){
                        equalityCheckNbr ++;
                    }
                }
            }
            boolean equalityCheck = (equalityCheckNbr == this.options.size());
            boolean sizeCheck = (this.options.size() == menu.getOptions().size());

            return (equalityCheck && sizeCheck);
        }
        else{
            return false;
        }

    }

    public static void displayInRecap(List<HashMap<String, String>> list){
        ArrayList<Menu> menusInRecap = new ArrayList<>();
        ArrayList<Integer> menuNumbers = new ArrayList<>();

        for (Menu menu : Orders.getActiveOrder().getOrder()){
            boolean added = false;
            for (int i = 0; i < menusInRecap.size() ; i++){
                if (menusInRecap.get(i).equals(menu)){
                    int actualValue = menuNumbers.get(i);
                    menuNumbers.set(i, actualValue+1);
                    added = true;
                }
            }
            if (!added){
                menusInRecap.add(menu);
                menuNumbers.add(1);
            }
        }

        for(int i = 0; i < menusInRecap.size() ; i++){
            Menu menu = menusInRecap.get(i);
            int menuNbr = menuNumbers.get(i);

            String food = String.valueOf(menuNbr) + "x " + menu.getFood().toString();
            double priceNbr = menuNbr * menu.getFood().getPrice();
            String price = String.format("%.2f", priceNbr);
            price = price + Orders.getMoneyDevise();

            String option;
            if (menu.getOptions().size() == 0){
                option = "No options selected.";
            }
            else{
                option = "Options:  ";
                for( OptionsTypes opt : menu.getOptions()){
                    option = option + opt.toString() + " ; " ;
                }
            }

            HashMap<String, String> element = new HashMap<>();
            element.put("elem", food);
            element.put("price", price);
            element.put("options", option);

            list.add(element);
        }
    }
}