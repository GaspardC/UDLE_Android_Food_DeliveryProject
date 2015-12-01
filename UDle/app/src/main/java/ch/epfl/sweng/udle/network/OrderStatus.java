package ch.epfl.sweng.udle.network;

/**
 * Created by rodri on 27/11/2015.
 */
public enum OrderStatus {
    WAITING ("Waiting"),
    ENROUTE ("EnRoute"),
    DELIVERED("Delivered");

    private String name = "";

    OrderStatus(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
