package ch.epfl.sweng.udle.network;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Gasp on 14/01/16.
 */
@ParseClassName("ParseRestaurantMark")
public class ParseRestaurantMark extends ParseObject {

    /**
     * Default constructor is required by Parse.
     */
    public ParseRestaurantMark() {
        //Empty
    }
    public int getAverage() {
        return getInt("mark");
    }

    public int getNumberMarks() {
        return getInt("numberOfMark");
    }

    public void incrementMark() {
        increment("numberOfMark");
    }

    public void setAverage(int newMark) {
        put("mark",newMark);
    }
}
