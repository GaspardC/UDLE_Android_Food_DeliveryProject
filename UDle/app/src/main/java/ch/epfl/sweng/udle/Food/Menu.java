package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 */
public class Menu {

    private FoodTypes food;
    private ArrayList<OptionsTypes> options;
    private ArrayList<DrinkTypes> drinks;


    public Menu(){
        this.food = null;
        this.options = new ArrayList<>();
        this.drinks = new ArrayList<>();
    }


    public FoodTypes getFood(){
        return food;
    }
    public void setFood(FoodTypes food){
        this.food = food;
    }


    public ArrayList<OptionsTypes> getOptions(){
        return options;
    }
    public void addToOptions(OptionsTypes options){
        this.options.add(options);
    }
    public void removeFromOptions(OptionsTypes options){
        this.options.remove(options);
    }


    public ArrayList<DrinkTypes> getDrinks(){
        return drinks;
    }
    public void addToDrinks(DrinkTypes drink){
        this.drinks.add(drink);
    }
    public void removeFromDrinks(DrinkTypes drink){
        this.drinks.remove(drink);
    }

}
