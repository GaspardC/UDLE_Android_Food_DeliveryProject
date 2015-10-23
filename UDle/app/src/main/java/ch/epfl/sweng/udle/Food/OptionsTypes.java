package ch.epfl.sweng.udle.Food;

/**
 * Created by rodri on 23/10/2015.
 */
public enum OptionsTypes {

    SALAD("Salad"),
    TOMATO("Tomato"),
    OIGNON("Oignon");

    private String name = "";

    OptionsTypes(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
