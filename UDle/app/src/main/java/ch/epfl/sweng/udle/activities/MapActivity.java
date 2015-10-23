package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.udle.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }


    /** Called when the user clicks the MenuMap_ValidatePosition button */
    public void goToMenuActivity(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        //Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

}

