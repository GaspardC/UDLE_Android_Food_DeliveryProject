package ch.epfl.sweng.udle.network;

/**
 * Created by Johan on 08.11.2015.
 */
public enum UserStatus {
    GUEST ("Guest"),
    CUSTOMER ("Customer"),
    RESTAURANT_OWNER ("Restaurant owner");

    private String name = "";

    UserStatus(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
