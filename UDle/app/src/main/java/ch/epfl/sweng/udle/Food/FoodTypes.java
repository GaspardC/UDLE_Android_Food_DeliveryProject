package ch.epfl.sweng.udle.Food;

/**
 * Created by rodri on 23/10/2015.
 *
 * Represents the different type of food available.
 *
 */
public enum FoodTypes {

    KEBAB("Kebab", 10.00, 10),
    PIZZA("Pizza", 10.00, 10),
    BURGER("Burger", 10.00, 10);

    private String name = "";
    private double price = -1;
    private int maxNbr = -1;

    FoodTypes(String name, double price, int maxNbr){
        this.name = name;
        this.price = price;
        this.maxNbr = maxNbr;
    }

    /**
     * @return The price of the specified food
     */
    public double getPrice(){
        return price;
    }


    /**
     * @return String representation of the food (The name of the food).
     */
    public String toString(){
        return name;
    }


    /**
     * @return The maximum number of menus a user can order with this food as main course.
     */
    public int getMaxNbr(){
        return maxNbr;
    }

}
