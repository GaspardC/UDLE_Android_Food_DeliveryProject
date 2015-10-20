package ch.epfl.sweng.udle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    /** Called when the user clicks the MapActivity button */
    public void goToDrinkActivity(View view) {
        Intent intent = new Intent(this, DrinkActivity.class);
        startActivity(intent);
    }
}
