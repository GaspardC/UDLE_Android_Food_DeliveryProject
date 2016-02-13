package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodri on 23/10/2015.
 * A Menu represent a food (kebab, burger) with the options (e.g salad, tomato)
 */
public class Menu {

    private FoodTypes food;
    private ArrayList<OptionsTypes> options;

    public Menu() {
        this.food = null;
        this.options = new ArrayList<>();
    }


    /**
     * @return The foodType (Burger or Kebab)
     */
    public FoodTypes getFood() {
        return food;
    }

    /**
     * @param food The foodType for this menu (Kebab or burger)
     */
    public void setFood(FoodTypes food) {
        if (food == null) {
            throw new IllegalArgumentException("Try to set the foodType of a menu to null.");
        }
        this.food = food;
    }


    /**
     * @return An ArrayList of all options (Salad, Tomato, Ketchup) for this Menu.
     */
    public ArrayList<OptionsTypes> getOptions() {
        return options;
    }


    /**
     * @param option Option to add for this menu
     */
    public void addToOptions(OptionsTypes option) {
        if (option == null) {
            throw new IllegalArgumentException("Try to add a null option to the optionsType List.");
        }
        if (!this.options.contains(option)) {
            this.options.add(option);
        }
    }

    /**
     * @param option The option to remove the this menu
     */
    public void removeFromOptions(OptionsTypes option) {
        if (option == null) {
            throw new IllegalArgumentException("Try to remove a null option to the optionsType List.");
        }
        if (this.options.contains(option)) {
            this.options.remove(option);
        }
    }

    /**
     * Overide the equals object.
     * In order to check if two menus are equals, check the food and all the options.
     * @param menu The menu to compare with the current one
     * @return True is the two menu are equals, false otherway.
     */
    public boolean equals(Menu menu) {
        if (menu == null  ||  menu.isEmpty()){
            throw new IllegalArgumentException("Invalid menu passd in parameter.");
        }
        if (isEmpty()){
            throw new IllegalStateException("The menu is still empty.");
        }

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

    /**
     * @return True if the menu is empty ( no food and no options)
     */
    public boolean isEmpty(){
        if (this.food == null && this.options.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Add display in recap for ALL Menu in the order into the HashMap responsible of the display of the all recap.
     * @param list HashMap used in the recaps of the order
     * @param noOption String to display under the Food type if no option are selected for the menu
     * @param options String to display under the Food type if at least 1 option is selected for the menu
     */
    public static void displayInRecap(List<HashMap<String, String>> list, String noOption, String options){
        ArrayList<Menu> menusInRecap = new ArrayList<>();
        ArrayList<Integer> menuNumbers = new ArrayList<>();

        for (Menu menu : Orders.getActiveOrder().getMenus()){
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

            String food = String.valueOf(menuNbr) + " " + menu.getFood().toString();
            double priceNbr = menuNbr * menu.getFood().getPrice();
            String price = String.format("%.2f", priceNbr);
            price = price + Orders.getMoneyDevise();
            price = price.replace(",",".");
            String option;
            if (menu.getOptions().size() == 0){
                option = noOption;
            }
            else{
                //option = Resources.getSystem().getString(R.string.options);
                option = options;

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