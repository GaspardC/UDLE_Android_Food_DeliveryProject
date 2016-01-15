package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;

public class CreditCardActivity extends SlideMenuActivity {

    private Button addACreditCard;
    private TextView infoCreditCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        addACreditCard = (Button) findViewById(R.id.CreditCardActivityButtonAdd);
        setButtonBehavior();
    }

    private void setButtonBehavior() {
        addACreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditCardActivity.this, EnterNumbersCreditCard.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        infoCreditCard = (TextView) findViewById(R.id.CreditCardActivityTextViewNoCreditCard);

        String customerId = DataManager.getCustomerId();
        if(customerId == null){
            infoCreditCard.setText("No credit card added");
            return;
        }
        String last4 = DataManager.getLast4();
        infoCreditCard.setText("card added : **** **** **** " + last4);

    }
}
