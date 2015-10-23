package ch.epfl.sweng.udle.Food;

/**
 * Created by rodri on 23/10/2015.
 */
public enum MoneyDevise {
    CHF("CHF"),
    DOLLAR("$"),
    EURO("€"),
    STERLING("£");

    private String symbol = "";

    MoneyDevise(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }
}
