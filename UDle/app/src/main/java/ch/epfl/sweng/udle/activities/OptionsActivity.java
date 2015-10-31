package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    /** Called when the user clicks the MapActivity button */
    public void goToDrinkActivity(View view) {
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menu = orderElement.getOrder();
        //TODO: use the function menu.addToOptions to add the selected options. Be careful to add the option with the correct menu (in case of >1 menu)


        //TODO If the user wants multiple menus, need to add specific options to each of this menus.
        Intent intent = new Intent(this, DrinkActivity.class);
        startActivity(intent);
    }
}
