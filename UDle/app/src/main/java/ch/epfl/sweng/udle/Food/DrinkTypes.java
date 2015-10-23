package ch.epfl.sweng.udle.Food;

/**
 * Created by rodri on 23/10/2015.
 */
public enum DrinkTypes {

    WATER("Water",5.00),
    COCA("Coca", 5.00),
    BEER("Beer", 5.00);

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
}
