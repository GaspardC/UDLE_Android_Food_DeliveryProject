package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class DrinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
    }

    public void goToRecapActivity(View view){
        OrderElement orderElement = Orders.getActiveOrder();
        //TODO: orderElement.addToDrinks to add the choosen drinks (if any) to the order.

        Intent intent = new Intent(this, RecapActivity.class);
        startActivity(intent);
    }
}
