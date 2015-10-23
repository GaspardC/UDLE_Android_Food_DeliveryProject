package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.udle.R;

public class RecapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);
    }

    public void gotToPaymentActivity(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}
