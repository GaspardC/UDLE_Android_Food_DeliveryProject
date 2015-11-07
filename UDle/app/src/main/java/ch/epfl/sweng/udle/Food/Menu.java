package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;

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
}