package ch.epfl.sweng.udle.Food;

/**
 * Created by rodri on 23/10/2015.
 */
public enum FoodTypes {

    KEBAB("Kebab", 10.00, 10),
    BURGER("Burger", 10.00, 10);

    private String name = "";
    private double price = -1;
    private int maxNbr = -1;

    FoodTypes(String name, double price, int maxNbr){
        this.name = name;
        this.price = price;
        this.maxNbr = maxNbr;
    }

    public double getPrice(){
        return price;
    }
    public String toString(){
        return name;
    }
    public int getMaxNbr(){
        return maxNbr;
    }

}
