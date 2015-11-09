package ch.epfl.sweng.udle;

import org.junit.Test;


import ch.epfl.sweng.udle.Food.OptionsTypes;

import static junit.framework.Assert.assertEquals;

/**
 * Created by rodri on 30/10/2015.
 */
public class OptionsTypesTest {

    @Test
    public void saladTestToString(){
        OptionsTypes options = OptionsTypes.SALAD;

        assertEquals("Salad", options.toString());
    }

    @Test
    public void tomatoTestToString(){
        OptionsTypes options = OptionsTypes.TOMATO;

        assertEquals("Tomato", options.toString());
    }

    @Test
    public void oignonTestToString(){
        OptionsTypes options = OptionsTypes.OIGNON;

        assertEquals("Oignon", options.toString());
    }

    @Test
    public void algerienneTestToString(){
        OptionsTypes options = OptionsTypes.ALGERIENNE;

        assertEquals("Sauce Algerienne", options.toString());
    }

    @Test
    public void mustardTestToString(){
        OptionsTypes options = OptionsTypes.MUSTARD;

        assertEquals("Mustard", options.toString());
    }

    @Test
    public void mayoTestToString(){
        OptionsTypes options = OptionsTypes.MAYO;

        assertEquals("Mayo", options.toString());
    }

    @Test
    public void ketchupTestToString(){
        OptionsTypes options = OptionsTypes.KETCHUP;

        assertEquals("Ketchup", options.toString());
    }
}