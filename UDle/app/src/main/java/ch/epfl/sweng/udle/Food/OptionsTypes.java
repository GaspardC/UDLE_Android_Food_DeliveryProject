package ch.epfl.sweng.udle.Food;

/**
 * Created by rodri on 23/10/2015.
 *
 * Represents the different types of options available for a foodType
 */
public enum OptionsTypes {

    Frites("Frites"),
    Potatoes("Potatoes"),
    KETCHUP("Ketchup"),
    MAYO("Mayo");

    private String name = "";

    OptionsTypes(String name){
        this.name = name;
    }

    /**
     * @return Name of the options as a String.
     */
    public String toString(){
        return name;
    }
}
