package ch.epfl.sweng.udle.Food;

import java.util.ArrayList;

/**
 * Created by rodri on 23/10/2015.
 */
public class Menu {

    private FoodTypes food;
    private ArrayList<OptionsTypes> options;

    public Menu(){
        this.food = null;
        this.options = new ArrayList<>();
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
    public void addToOptions(OptionsTypes option){
        if (! this.options.contains(option)){
            this.options.add(option);
        }
    }
}
